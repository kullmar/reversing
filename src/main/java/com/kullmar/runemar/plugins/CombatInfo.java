package com.kullmar.runemar.plugins;

import com.kullmar.runemar.graphics.PaintListener;

import java.awt.*;

public class CombatInfo implements PaintListener {

    @Override
    public void onPaint(Graphics g) {
        g.drawRect(10, 10, 50, 50);
    }
}
