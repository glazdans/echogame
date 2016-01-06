package com.glazdans.echo.bullet.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.glazdans.echo.bullet.BulletTestScreen;
import com.glazdans.echo.bullet.GameObject;

public class PlayerController extends InputAdapter{
    GameObject gameObject;
    Camera camera;
    btCollisionWorld world;

    public PlayerController(GameObject gameObject, Camera camera,btCollisionWorld world) {
        this.gameObject = gameObject;
        this.camera = camera;
        this.world = world;
    }

    private boolean pressUp;
    private boolean pressDown;
    private boolean pressLeft;
    private boolean pressRight;

    private boolean pressShoot;

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


        Vector3 floor = floorPosition(world);
        floor = floor.sub(gameObject.position);
        cameraVector.set(floor.x,floor.z);
        gameObject.setRotation(-cameraVector.angle()+90);

        tmp.set(0,0,0);

        if(pressShoot) {
            //gameObject.shoot();
        }
    }
    private static final Vector2 cameraVector= new Vector2();

    private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(new Vector3(), new Vector3());

    public Vector3 floorPosition(btCollisionWorld collisionWorld){
        Ray cameraRay = camera.getPickRay(Gdx.input.getX(),Gdx.input.getY());
        callback.setCollisionObject(null);
        callback.setClosestHitFraction(10);
        callback.setRayFromWorld(cameraRay.origin);
        callback.setRayToWorld(cameraRay.getEndPoint(new Vector3(),50));

        collisionWorld.rayTest(cameraRay.origin,cameraRay.getEndPoint(new Vector3(),50),callback);

        Vector3 returnVector = tmp;
        callback.getHitPointWorld(returnVector);
        return returnVector;

    }

    private static Vector3 tmp = new Vector3();
    private static Vector3 tmp1 = new Vector3();
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (button){
            case Input.Buttons.LEFT:
                pressShoot = true;
                break;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button){
            case Input.Buttons.LEFT:
                pressShoot = false;
                break;
        }

        return false;
    }
}
