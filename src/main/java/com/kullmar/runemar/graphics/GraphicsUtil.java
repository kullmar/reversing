package com.kullmar.runemar.graphics;


import java.awt.*;
import java.util.List;

public class GraphicsUtil {
    public static void drawMultiLineString(Graphics g, List<String> lines, Point pos) {
        for (String line : lines) {
            g.drawString(line, pos.x, pos.y += g.getFontMetrics().getHeight());
        }
    }
}
