package com.kullmar.runemar.tbd;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kullmar.runemar.api.LocalPoint;
import com.kullmar.runemar.api.RSClient;
import com.kullmar.runemar.api.RSPlayer;
import com.kullmar.runemar.plugins.NearbyPlayers;
import com.kullmar.runemar.plugins.RandomClickerPlugin;
import com.kullmar.runemar.rs.RSLoader;

import javax.swing.*;
import java.applet.Applet;
import java.util.List;
import java.util.Random;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class RuneMar {
    private JFrame window;
    private RSClient client;

    private static Injector injector;

    public static void main(String[] args) {
        injector = Guice.createInjector(new RuneMarModule());
        injector.getInstance(RuneMar.class).start();
    }

    public void start() {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private void createAndShowGui() {
        window = new JFrame("RuneMar");
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Applet rsApplet = new RSLoader().load();
        client = (RSClient) rsApplet;

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(new ClientPanel(rsApplet));

        window.add(container);
        window.pack();
        window.setMinimumSize(rsApplet.getPreferredSize());
        window.setVisible(true);
        addPlugins();
    }

    private void addPlugins() {
        waitForCanvas();
        // printPlayerLocation();
        // printCameraLocation();
        // NearbyPlayers nearbyPlayers = new NearbyPlayers(client);
        new RandomClickerPlugin(window);
    }

    private void printCameraLocation() {
        new Thread(() -> {
            while (true) {
                if (client.getLocalPlayer() != null) {
                    System.out.println(String.format("(%d, %d, %d)", client.getCameraX(), client.getCameraY(),
                            client.getCameraZ()));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void printPlayerLocation() {
        new Thread(() -> {
            while (true) {
                if (client.getLocalPlayer() != null) {
                    LocalPoint location = client.getLocalPlayer().getLocation();
                    if (location != null) {
                        System.out.println(location);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void waitForCanvas() {
        while (client.getCanvas() == null) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printPlayer() {
        while(true) {
            if (client.getLocalPlayer() != null) {
                if (client.getLocalPlayer().getName() != null) {
                    System.out.println(client.getLocalPlayer().getName());
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printCachedPlayers() {
        while (true) {
            List<RSPlayer> players = client.getPlayers();
            if (players != null && !((List) players).isEmpty()) {
                for (RSPlayer player : players) {
                    if (player != null) {
                        System.out.println(player.getName());
                    }
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
