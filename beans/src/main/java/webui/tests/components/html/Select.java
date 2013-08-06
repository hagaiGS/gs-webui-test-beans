package webui.tests.components.html;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import webui.tests.components.abstracts.AbstractComponent;

import java.util.List;

public class Select  extends AbstractComponent<Select> {

    @FindBy(css="option")
    public List<WebElement> options;

    public WebElement selectOption( String value ){
        return selectOption( By.valueAttribute(value) ) ;
    }

    public WebElement selectOption(By by) {
        WebElement option = getOption(by);
        if ( !option.isSelected() ){
            option.click();
        }
        return option;
    }

    public WebElement getOption( By by ){
        for (int i = 0; i < options.size(); i++) {
            WebElement option = options.get(i);
            if (by.suitable(option,i)) {
                return option;
            }
        }
        return null;
    }

    public List<WebElement> getOptionElements(){
        return options;
    }

    public WebElement getSelected() {
        return getOption( By.selected(true));
    }


    public static abstract class By{

        public abstract boolean suitable( WebElement option, int index );

        public static By textContains( String contains ){
            return new TextContains( contains );
        }

        public static By selected( boolean selected ){
            return new Selected( selected );
        }

        public static By index( int location ){
            return new IndexLocation( location );
        }

        public static By valueAttribute( String value ){
            return new ValueAttribute( value );
        }

        public static class IndexLocation extends By{
            public int index;

            public IndexLocation(int index) {
                this.index = index;
            }

            @Override
            public boolean suitable(WebElement option, int index) {
                return index == this.index;
            }
        }

        public static class Selected extends By{

            boolean selected;

            public Selected(boolean selected) {
                this.selected = selected;
            }

            @Override
            public boolean suitable(WebElement option, int index) {
                return option.isSelected() == selected;
            }
        }

        public static class TextContains extends By{
            public String contains;

            public TextContains(String contains) {
                this.contains = contains;
            }

            @Override
            public boolean suitable(WebElement option, int index) {
                return StringUtils.containsIgnoreCase( option.getText(), contains );
            }
        }

        public static class ValueAttribute extends By{
            public String value;

            public ValueAttribute(String value) {
                this.value = value;
            }

            @Override
            public boolean suitable(WebElement option, int index) {
                return StringUtils.equalsIgnoreCase(option.getAttribute("value"), value);
            }
        }
    }
}
