package com.glazdans.echo.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
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
        ActionEvent actionEvent = new ActionEvent();
        actionEvent.direction.set(tmp);
        actionEvent.shoot = pressShoot;
        actionEvent.id = player1Id;
        eventDispatcher.addEvent(actionEvent);

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
                break;
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
