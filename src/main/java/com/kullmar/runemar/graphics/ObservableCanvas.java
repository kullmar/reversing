package com.kullmar.runemar.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ObservableCanvas extends Canvas {
    private BufferedImage debugImage;
    private static List<PaintListener> paintListeners = new ArrayList<>();

    @Override
    public Graphics getGraphics() {
        if (debugImage == null || debugImage.getWidth() != getWidth() || debugImage.getHeight() != getHeight()) {
            debugImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        }

        Graphics g = debugImage.getGraphics();

        for (PaintListener paintListener : paintListeners) {
            paintListener.onPaint(g);
        }

        super.getGraphics().drawImage(debugImage, 0, 0, null);
        return g;
    }

    public void addPaintListener(PaintListener paintListener) {
        if (!paintListeners.contains(paintListener)) {
            paintListeners.add(paintListener);
        }
    }
}
