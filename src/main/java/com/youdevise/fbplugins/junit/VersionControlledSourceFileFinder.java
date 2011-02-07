package com.youdevise.fbplugins.junit;

public class VersionControlledSourceFileFinder {

    private final PluginProperties properties;

    public VersionControlledSourceFileFinder(PluginProperties properties) {
        this.properties = properties;
    }

    public String location(String fullFilePath) {
        String vcsRoot = properties.versionControlHttpHost() + properties.versionControlProjectRoot() + "/";

        String vcsSegmentOfFileName = fullFilePath.substring(fullFilePath.lastIndexOf(properties.projectBaseDirName()));

        return vcsRoot + vcsSegmentOfFileName;
    }

}
