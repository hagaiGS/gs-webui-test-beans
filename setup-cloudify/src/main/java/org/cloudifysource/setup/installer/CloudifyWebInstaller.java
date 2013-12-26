package org.cloudifysource.setup.installer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * User: guym
 * Date: 8/7/13
 * Time: 12:41 PM
 */
public abstract class CloudifyWebInstaller implements CloudifyInstaller {

    private static Logger logger = LoggerFactory.getLogger(CloudifyWebInstaller.class);

    public String getHtml( String url ){
        StringBuilder sb = new StringBuilder();
        try {
            URL myUrl = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            myUrl.openStream()));

            String line;

            while ((line = in.readLine()) != null)
                sb.append(line);

            in.close();
        } catch (Exception e) {
            logger.error("error while reading HTML from [{}]", url, e);
            throw new RuntimeException( String.format("error while reading HTML from [%s]", url ), e );
        }
        return sb.toString();
    }

    @Override
    public void invoke() {
        getCloudify();
    }

    public static enum SizeUnit{
        BYTE(1), KBYTE(  Math.pow(10,3) ), MBYTE( Math.pow(10,6) ), GBYTE( Math.pow(10,9) );

        public int factor;

        private SizeUnit(double factor) {
            this.factor = (int) factor;
        }

        public int convert( int number ){
            return number/ factor;
        }


        @Override
        public String toString() {
            if ( this == BYTE ){
                return "B";
            }else{
                return name().substring(0,2);
            }
        }
    }

    public int getSize(URL url) {
        return getSize( url, SizeUnit.MBYTE );

    }

    public int getSize( URL url , SizeUnit unit ){
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.getInputStream();
            return unit.convert(conn.getContentLength());
        } catch (IOException e) {
            return -1;
        } finally {
            conn.disconnect();
        }
    }


    public void downloadBinary(String fromUrl, File destination) {
        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;

        try {
            URL website = new URL(fromUrl);
            SizeUnit sizeUnit = SizeUnit.MBYTE;
            int byteSize = getSize(website, SizeUnit.BYTE );
            int size = sizeUnit.convert( byteSize );

            rbc = Channels.newChannel(website.openStream());

            // GUY - TODO - this is where we can implement a progress indicator.
            // the last value to "transferFrom" is the maximum byes to transfer.
            // we can do a loop that each iteration will transfer a small amount and will fire a progress event.
            // use the "getSize" to get a better progress indication.
            fos = new FileOutputStream(destination);

            logger.info("starting to download {}{}  from [{}] to [{}]", new Object[]{ size, sizeUnit, fromUrl, destination.getAbsolutePath()});

            long transferred = 0;
            long startTime = System.currentTimeMillis();

            while( transferred < byteSize ){
                transferred += fos.getChannel().transferFrom(rbc, transferred, sizeUnit.factor );
                long eta = ( byteSize - transferred ) / ( transferred / ( System.currentTimeMillis() - startTime ) );
                logger.info("downloaded [{}]. eta [{}] sec",transferred,  eta / 1000 );
            }
        } catch (Exception e) {
            logger.error("unable to download binary from [{}] to [{}]", fromUrl, destination.getAbsolutePath());
            throw new RuntimeException(String.format("unable to download binary from [%s] to [%s]", fromUrl, destination.getAbsolutePath()), e);
        }finally{
            try{
                if ( rbc != null ){
                    rbc.close();
                }
            }catch(Exception e){
                logger.error("unable to close rbc connection",e);
            }

            try{
                if ( fos != null ){
                    fos.close();
                }
            }catch(Exception e){
                logger.error("unable to close fos connection",e);
            }
        }
    }
}
