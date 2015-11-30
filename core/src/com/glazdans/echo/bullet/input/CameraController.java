package com.glazdans.echo.bullet.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;

public class CameraController extends InputAdapter {
    Camera camera;

    public CameraController(Camera camera) {
        this.camera = camera;
    }
}
