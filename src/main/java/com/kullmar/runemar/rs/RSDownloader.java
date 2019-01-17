package com.kullmar.runemar.rs;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class RSDownloader {
    private RSConfig rsConfig = new RSConfig();
    private static final String gamepackLocation = System.getProperty("user.home") + "/gamepack.jar";

    public void downloadClient() throws IOException {
        FileUtils.copyURLToFile(rsConfig.getGamepackUrl(), new File(gamepackLocation));
    }

    public static String getGamepackLocation() {
        return gamepackLocation;
    }
}
