package com.glazdans.echo.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.glazdans.echo.bullet.BulletTestScreen;
import com.glazdans.echo.bullet.Physics;
import com.glazdans.echo.component.TransformComponent;
import com.glazdans.echo.events.ActionEvent;
import com.glazdans.echo.events.EventDispatcher;

public class InputSystem extends BaseSystem implements InputProcessor {
    public boolean pressUp;
    public boolean pressDown;
    public boolean pressLeft;
    public boolean pressRight;
    public boolean pressShoot;

    private static Vector3 tmp = new Vector3();

    private EventDispatcher eventDispatcher;
    private int player1Id;

    public InputSystem() {
        eventDispatcher = EventDispatcher.getInstance();
    }

    public void setPlayer1Id(int id) {
        player1Id = id;
    }



    @Override
    protected void processSystem() { // TODO hold last event - if equal don't send
        tmp.set(0, 0, 0);
        if (pressUp) {
            tmp.add(0, 0, 1);
        }
        if (pressDown) {
            tmp.add(0, 0, -1);
        }
        if (pressLeft) {
            tmp.add(1, 0, 0);
        }
        if (pressRight) {
            tmp.add(-1, 0, 0);
        }

        Vector3 floor = floorPosition(Physics.getInstance().collisionWorld);
        Vector3 position = BulletTestScreen.world.getMapper(TransformComponent.class).get(player1Id).position;
        floor = floor.sub(position);
        cameraVector.set(floor.x,floor.z);
        float angle = -cameraVector.angle()+90;

        ActionEvent actionEvent = new ActionEvent();
        actionEvent.direction.set(tmp);
        actionEvent.shoot = pressShoot;
        actionEvent.id = player1Id;
        actionEvent.degree = angle;
        eventDispatcher.addEvent(actionEvent);

    }

    private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(new Vector3(), new Vector3());

    private static final Vector2 cameraVector= new Vector2();
    private static final Vector3 tmp1= new Vector3();
    private Vector3 floorPosition(btCollisionWorld collisionWorld){
        PerspectiveCamera camera = BulletTestScreen.camera; // TODO MAKE BETTER CAMERA HANDLING?
        Ray cameraRay = camera.getPickRay(Gdx.input.getX(),Gdx.input.getY());
        callback.setCollisionObject(null);
        callback.setClosestHitFraction(10);
        callback.setRayFromWorld(cameraRay.origin);
        callback.setRayToWorld(cameraRay.getEndPoint(new Vector3(),50));
        callback.setCollisionFilterGroup(Physics.OBJECT_FLAG);
        callback.setCollisionFilterMask(Physics.ALL_FLAG);

        collisionWorld.rayTest(cameraRay.origin,cameraRay.getEndPoint(new Vector3(),50),callback);

        Vector3 returnVector = tmp1;
        callback.getHitPointWorld(returnVector);
        return returnVector;

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
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
        switch (keycode) {
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
        switch (button) {
            case Input.Buttons.LEFT:
                pressShoot = true;
                return false;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Input.Buttons.LEFT:
                pressShoot = false;
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
