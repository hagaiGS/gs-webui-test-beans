package org.cloudifysource.setup;

import webui.tests.setup.actions.SetupActions;

import java.io.File;

/**
 * User: guym
 * Date: 11/28/13
 * Time: 3:13 PM
 */
public class BuildProperties implements SetupActions.ExecuteCommandAction.ExecFileGetter{

    public String buildArtifact;
    public String localBuildArtifactCopy;
    public String outputFolder;
    public File cliFile;

    public String getBuildArtifact() {
        return buildArtifact;
    }

    public void setBuildArtifact(String buildArtifact) {
        this.buildArtifact = buildArtifact;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public File getCliFile() {
        return cliFile;
    }

    public void setCliFile(File cliFile) {
        this.cliFile = cliFile;
    }

    public String getLocalBuildArtifactCopy() {
        return localBuildArtifactCopy;
    }

    public void setLocalBuildArtifactCopy(String localBuildArtifactCopy) {
        this.localBuildArtifactCopy = localBuildArtifactCopy;
    }

    @Override
    public File getExecutableFile() {
        return cliFile;
    }
}
