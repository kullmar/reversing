package com.kullmar.runemar.tbd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.*;
import java.util.zip.ZipEntry;

import static org.apache.commons.io.IOUtils.toByteArray;

public class JarUtils {
    public static JarFile loadJarFromPath(String path) throws IOException {
        return new JarFile(new File(path));
    }

    public static Map<String, byte[]> getClasses(String jarPath) throws IOException {
        Map<String, byte[]> jarClasses = new HashMap<>();
        JarFile jar = loadJarFromPath(jarPath);
        Enumeration<JarEntry> jarEntryEnumeration = jar.entries();
        while (jarEntryEnumeration.hasMoreElements()) {
            JarEntry jarEntry = jarEntryEnumeration.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".class")) {
                jarClasses.put(name, toByteArray(jar.getInputStream(jarEntry)));
            }
        }
        return jarClasses;
    }

    public static Map<String, byte[]> getNonClasses(String jarPath) throws IOException {
        Map<String, byte[]> nonClassEntries = new HashMap<>();
        JarFile jar = loadJarFromPath(jarPath);
        Enumeration<JarEntry> jarEntryEnumeration = jar.entries();
        while (jarEntryEnumeration.hasMoreElements()) {
            JarEntry jarEntry = jarEntryEnumeration.nextElement();
            String name = jarEntry.getName();
            if (!name.endsWith(".class")) {
                nonClassEntries.put(name, toByteArray(jar.getInputStream(jarEntry)));
            }
        }
        return nonClassEntries;
    }

    public static void writeNewJar(Map<String, byte[]> jarEntries, String dest) throws IOException {
        Manifest manifest = new Manifest();
        Attributes global = manifest.getMainAttributes();
        global.put(Attributes.Name.MANIFEST_VERSION, "1.0.0");
        JarOutputStream out = new JarOutputStream(new FileOutputStream(dest), manifest);
        for (String entry : jarEntries.keySet()) {
            out.putNextEntry(new ZipEntry(entry));
            out.write(jarEntries.get(entry));
            out.closeEntry();
        }
        out.close();
    }
}
