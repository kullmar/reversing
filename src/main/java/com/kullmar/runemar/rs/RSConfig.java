package com.kullmar.runemar.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class RSConfig {
    private static final String baseUrl = "https://oldschool51.runescape.com/";
    private HashMap<String, String> params = new HashMap<String, String>();

    public RSConfig() {
        parse();
    }

    public URL getGamepackUrl() throws MalformedURLException {
        return new URL(baseUrl + params.get("initial_jar"));
    }

    private void parse() {
        try {
            System.out.println("Parsing Website");
            final URLConnection urlConnection = new URL(baseUrl + "jav_config.ws").openConnection();
            urlConnection.addRequestProperty("User-Agent", "Opera/9.80 (Windows NT 6.1; U; en-GB) Presto/2.10.229 Version/11.61");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                line = line.replaceAll("\">'", "\"").replaceAll("'", "")
                        .replaceAll("\\(", "").replaceAll("\\)", "")
                        .replaceAll("\"", "").replaceAll(" ", "")
                        .replaceAll("param=", "").replaceAll(";", "")
                        .replaceAll("value", "");
                final String[] splitted = line.split("=");
                if (splitted.length == 1) {
                    params.put(splitted[0], "");
                } else if (splitted.length == 2) {
                    params.put(splitted[0], splitted[1]);
                } else if (splitted.length == 3) {
                    params.put(splitted[0], splitted[1] + "=" + splitted[2]);
                } else if (splitted.length == 4) {
                    params.put(splitted[0], splitted[1] + "=" + splitted[2] + "=" + splitted[3]);
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }


}
