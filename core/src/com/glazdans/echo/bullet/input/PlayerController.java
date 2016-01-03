package com.glazdans.echo.bullet.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.glazdans.echo.bullet.BulletTestScreen;
import com.glazdans.echo.bullet.GameObject;

public class PlayerController extends InputAdapter{
    GameObject gameObject;
    Camera camera;

    public PlayerController(GameObject gameObject, Camera camera) {
        this.gameObject = gameObject;
        this.camera = camera;
    }

    private boolean pressUp;
    private boolean pressDown;
    private boolean pressLeft;
    private boolean pressRight;

    public void updateMovement(){
        if(pressUp){
            tmp.add(0,0,1);
        }
        if(pressDown){
            tmp.add(0,0,-1);
        }
        if(pressLeft){
            tmp.add(1,0,0);
        }
        if(pressRight){
            tmp.add(-1,0,0);
        }
        gameObject.updateAcceleration(tmp);
        tmp.set(0,0,0);
    }

    private static Vector3 tmp = new Vector3();
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.W:
                pressUp = true;
                //tmp.set(camera.direction);
                break;
            case Input.Keys.A:
                pressLeft = true;
                break;
            case Input.Keys.S:
                pressDown = true;
                break;
            case Input.Keys.D:
                pressRight = true;
                break;

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.W:
                pressUp = false;
                break;
            case Input.Keys.A:
                pressLeft = false;
                break;
            case Input.Keys.S:
                pressDown = false;
                break;
            case Input.Keys.D:
                pressRight = false;
                break;

        }
        return false;
    }
}
