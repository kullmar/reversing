package com.kullmar.runemar.plugins;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.concurrent.ThreadLocalRandom;

public class RandomClickerPlugin
{
    private boolean running = false;

    private JFrame frame;

    public RandomClickerPlugin(JFrame frame) {
        this.frame = frame;
        running = true;
        new Thread(this::run).start();
    }

    public void run() {
        while (running) {
            clickRandomPoint();
            try {
                long sleep = ThreadLocalRandom.current().nextLong(10L*1000, 20L*1000);
                System.out.println("Sleeping for " + sleep/1000 + " seconds");
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void clickRandomPoint() {
        Dimension canvasSize = frame.getSize();
        int x = ThreadLocalRandom.current().nextInt(0, canvasSize.width);
        int y = ThreadLocalRandom.current().nextInt(0, canvasSize.height);
        Component component = frame.getContentPane();
        MouseEvent press = new MouseEvent(component, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(),
                0, x, y, 1, false, MouseEvent.BUTTON1);
        long sleep = ThreadLocalRandom.current().nextLong(20L, 200L);
        System.out.println("Waiting for " + sleep + " ms between mouse press and release");
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MouseEvent release = new MouseEvent(component, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(),
                0, x, y, 1, false, MouseEvent.BUTTON1);
        component.dispatchEvent(press);
        component.dispatchEvent(release);
//        clientUI.getFrame().dispatchEvent(click);
        System.out.println(String.format("Clicked (%d, %d)", x, y));
    }


    protected void shutDown()
    {
        running = false;
    }
}
