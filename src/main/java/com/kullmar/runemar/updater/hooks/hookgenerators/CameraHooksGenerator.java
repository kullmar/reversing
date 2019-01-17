package com.kullmar.runemar.updater.hooks.hookgenerators;

import com.kullmar.runemar.api.RSClient;
import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.asm.MethodInfo;
import com.kullmar.runemar.updater.hooks.FieldHook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kullmar.runemar.updater.finders.fieldfinders.CameraFieldsFinder.*;

public class CameraHooksGenerator implements HookGenerator {
    private FieldInfo cameraX;
    private FieldInfo cameraY;
    private FieldInfo cameraZ;
    private FieldInfo cameraYaw;
    private FieldInfo cameraPitch;

    @Override
    public List<FieldHook> run(Map<String, FieldInfo> identifiedFields) {
        List<FieldHook> fieldHooks = new ArrayList<>();
        cameraX = identifiedFields.get(CAMERA_X);
        cameraY = identifiedFields.get(CAMERA_Y);
        cameraZ = identifiedFields.get(CAMERA_Z);
        cameraYaw = identifiedFields.get(CAMERA_YAW);
        cameraPitch = identifiedFields.get(CAMERA_PITCH);
        fieldHooks.add(generateCameraXHook());
        fieldHooks.add(generateCameraYHook());
        fieldHooks.add(generateCameraZHook());
        fieldHooks.add(generateCameraYaw());
        fieldHooks.add(generateCameraPitch());
        return fieldHooks;
    }

    private FieldHook generateCameraXHook() {
        MethodInfo methodInfo = new MethodInfo("getCameraX", "()I");
        return new FieldHook(cameraX, RSClient.class, methodInfo, "client");
    }

    private FieldHook generateCameraYHook() {
        MethodInfo methodInfo = new MethodInfo("getCameraY", "()I");
        return new FieldHook(cameraY, RSClient.class, methodInfo, "client");
    }

    private FieldHook generateCameraZHook() {
        MethodInfo methodInfo = new MethodInfo("getCameraZ", "()I");
        return new FieldHook(cameraZ, RSClient.class, methodInfo, "client");
    }

    private FieldHook generateCameraYaw() {
        MethodInfo methodInfo = new MethodInfo("getCameraYaw", "()I");
        return new FieldHook(cameraYaw, RSClient.class, methodInfo, "client");
    }

    private FieldHook generateCameraPitch() {
        MethodInfo methodInfo = new MethodInfo("getCameraPitch", "()I");
        return new FieldHook(cameraPitch, RSClient.class, methodInfo, "client");
    }
}
