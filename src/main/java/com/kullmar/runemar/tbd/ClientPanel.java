package com.kullmar.runemar.tbd;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

final class ClientPanel extends JPanel
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public ClientPanel(Applet client)
    {
        Dimension appletSize = new Dimension(WIDTH, HEIGHT);
        setSize(appletSize);
        setMinimumSize(appletSize);
        setPreferredSize(appletSize);
        setLayout(new BorderLayout());
        setBackground(Color.black);

        if (client == null)
        {
            return;
        }

        client.setLayout(null);
        client.setSize(appletSize);

        client.init();
        client.start();

        add(client, BorderLayout.CENTER);
    }
}