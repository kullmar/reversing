package com.kullmar.runemar.api;

public class Camera {
    public final int x;
    public final int y;
    public final int z;
    public final int yaw;
    public final int pitch;

    public Camera(int x, int y, int z, int yaw, int pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
