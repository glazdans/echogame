package com.glazdans.echo.bullet.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.glazdans.echo.bullet.BulletTestScreen;

public class PlayerController extends InputAdapter{
    BulletTestScreen.GameObject gameObject;
    Camera camera;

    public PlayerController(BulletTestScreen.GameObject gameObject, Camera camera) {
        this.gameObject = gameObject;
        this.camera = camera;
    }
    private static Vector3 tmp = new Vector3();
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.W:
                tmp.set(camera.direction);
                gameObject.rigidBody.applyCentralForce(tmp.scl(100f));
                break;
            case Input.Keys.A:
                break;
            case Input.Keys.S:
                break;
            case Input.Keys.D:
                break;

        }
        return false;
    }
}
