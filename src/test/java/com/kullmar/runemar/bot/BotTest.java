package com.kullmar.runemar.bot;

import javafx.geometry.BoundingBox;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static junit.framework.TestCase.assertTrue;

public class BotTest {
    JFrame frame;
    JPanel container;
    BasicBot bot = new BasicBot();

    @Before
    public void setup() {
        SwingUtilities.invokeLater(this::createAndShowGui);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        frame.dispose();
    }

    private void createAndShowGui() {
        frame = new JFrame("Bot Test");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        container = new JPanel();
        container.setPreferredSize(new Dimension(800, 600));

        frame.add(container);
        frame.pack();
        frame.setVisible(true);

        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Graphics2D g = (Graphics2D) container.getGraphics();
                g.setColor(Color.RED);
                Point point = e.getPoint();
                g.fillOval(point.x, point.y, 2, 2);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    @Test
    public void createAndVisualizeClickPoint() {
        int x = 200;
        int y = 200;
        int width = 400;
        int height = 200;
        BoundingBox boundingBox = new BoundingBox(x, y, width, height);
        Graphics2D g = (Graphics2D) container.getGraphics();
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.setColor(Color.RED);
        for (int i = 0; i < 10000; ++i) {
            Point point = bot.createClickPoint(boundingBox);
            assertTrue(point.x >= x && point.x <= x + width && point.y >= y && point.y <= y + height);
            click(point);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void click(Point point) {
        MouseEvent click = new MouseEvent(container, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, point.x,
                point.y, 1, false, MouseEvent.BUTTON1);
        frame.dispatchEvent(click);
    }
}
