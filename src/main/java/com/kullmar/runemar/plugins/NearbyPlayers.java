package com.kullmar.runemar.plugins;

import com.google.common.eventbus.Subscribe;
import com.kullmar.runemar.api.RSClient;
import com.kullmar.runemar.api.RSPlayer;
import com.kullmar.runemar.graphics.PaintListener;
import com.kullmar.runemar.rs.RSLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.kullmar.runemar.graphics.GraphicsUtil.drawMultiLineString;

public class NearbyPlayers implements PaintListener, Runnable {
    boolean active = true;
    private RSClient rsClient;
    private List<String> cachedNames = new ArrayList<>();

    public NearbyPlayers(RSClient rsClient) {
        this.rsClient = rsClient;
        rsClient.getCanvas().addPaintListener(this);
        new Thread(this).start();
    }

    @Override
    @Subscribe
    public void onPaint(Graphics g) {
        renderBackground(g);
        Point pos = new Point(50, 50);
        drawMultiLineString(g, cachedNames, pos);
    }

    private void renderBackground(Graphics g) {
        Color inheritedColor = g.getColor();
        g.setColor(new Color(106, 106, 106, 127));
        g.fillRect(50, 50, 200, 200);
        g.setColor(inheritedColor);
    }

    private void updatePlayers() {
        List<RSPlayer> players = rsClient.getPlayers();
        players.removeIf(player -> (Objects.isNull(player)));
        players.removeIf(player -> Objects.isNull(player.getName()));
        cachedNames = players
                .stream()
                .map(player -> player.getName())
                .collect(Collectors.toList());
    }

    @Override
    public void run() {
        while (active) {
            updatePlayers();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
