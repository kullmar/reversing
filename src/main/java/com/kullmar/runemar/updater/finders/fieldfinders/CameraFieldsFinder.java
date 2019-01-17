package com.kullmar.runemar.updater.finders.fieldfinders;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;

import java.util.HashMap;
import java.util.Map;

public class CameraFieldsFinder implements FieldFinder {
    public static final String CAMERA_X = "cameraX";
    public static final String CAMERA_Y = "cameraY";
    public static final String CAMERA_Z = "cameraZ";
    public static final String CAMERA_YAW = "cameraYaw";
    public static final String CAMERA_PITCH = "cameraPitch";

    @Override
    public Map<String, FieldInfo> run(ClassCollection classCollection) {
        Map<String, FieldInfo> mapping = new HashMap<>();
        mapping.put(CAMERA_X, findCameraX());
        mapping.put(CAMERA_Y, findCameraY());
        mapping.put(CAMERA_Z, findCameraZ());
        mapping.put(CAMERA_YAW, findCameraYaw());
        mapping.put(CAMERA_PITCH, findCameraPitch());
        return mapping;
    }

    private FieldInfo findCameraX() {
        return new FieldInfo("hr","fw",  "I", -1604029815, true);
    }

    private FieldInfo findCameraY() {
        return new FieldInfo("hw", "au", "I", -414697091, true);
    }

    private FieldInfo findCameraZ() {
        return new FieldInfo("he","gl",  "I", -1021742515, true);
    }

    private FieldInfo findCameraYaw() {
        return new FieldInfo("hb","v",  "I", 999670937, true);
    }

    private FieldInfo findCameraPitch() {
        return new FieldInfo("hf","bv",  "I", 964567305, true);
    }
}
