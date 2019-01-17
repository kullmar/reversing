package com.kullmar.runemar.api;

public interface RSActor {
    int getX();

    int getY();

    default LocalPoint getLocation() {
        return new LocalPoint(getX(), getY());
    }
}
