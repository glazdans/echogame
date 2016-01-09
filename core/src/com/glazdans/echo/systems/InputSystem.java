package com.glazdans.echo.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.InputAdapter;
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

    public InputSystem(){
        eventDispatcher = EventDispatcher.getInstance();
    }

    @Override
    protected void processSystem() {
        tmp.set(0,0,0);
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

        ActionEvent actionEvent = new ActionEvent();
        actionEvent.direction.set(tmp);
        actionEvent.shoot = pressShoot;
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
