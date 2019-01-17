package com.kullmar.runemar.rs;

import java.applet.Applet;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class RSLoader {
    public Applet load() {
        URLClassLoader classLoader = null;
        try {
            classLoader = new URLClassLoader(new URL[]{new URL("file:./transformed.jar")});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Applet applet = null;
        try {
            applet = (Applet) classLoader.loadClass("client").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        AppletStub stub = new RSAppletStub();
        applet.setStub(stub);
        return applet;
    }
}
