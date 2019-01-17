package com.kullmar.runemar.bot;

import com.kullmar.runemar.api.InteractableObject;
import com.kullmar.runemar.api.LocalPoint;
import javafx.geometry.BoundingBox;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class BasicBot implements Bot {
    @Override
    public void interactWith(InteractableObject object) {

    }

    @Override
    public void moveTo(LocalPoint location) {

    }

    public Point createClickPoint(BoundingBox boundingBox) {
        return truncatedGaussian(boundingBox);
    }

    public static Point truncatedGaussian(BoundingBox boundingBox) {
        double avgX = 0.5d * (boundingBox.getMinX() + boundingBox.getMaxX());
        double avgY = 0.5d * (boundingBox.getMinY() + boundingBox.getMaxY());
        int x =
                (int) Math.min(Math.max(avgX + ThreadLocalRandom.current().nextGaussian()*boundingBox.getWidth() * 0.15d,
                boundingBox.getMinX()), boundingBox.getMaxX());
        int y =
                (int) Math.min(Math.max(avgY + ThreadLocalRandom.current().nextGaussian()*boundingBox.getHeight() * 0.15d,
                boundingBox.getMinY()), boundingBox.getMaxY());
        return new Point(x, y);
    }
}
