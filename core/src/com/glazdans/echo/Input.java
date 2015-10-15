package com.glazdans.echo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Input extends InputAdapter {
    private int entityId;
    private Command command;
    private Entity entity;

    private Vector3 tmp = new Vector3();
    private Vector2 directionVector = new Vector2();

    boolean up, down, left, right;

    public Input(int entityId) {
        this.entityId = entityId;
        command = new Command();
        command.entityId = entityId;
        entity = World.currentInstance().entitiesById.get(entityId);
        command.position.set(entity.position);
    }

    public void updateInput(float delta) {
        tmp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        tmp = DebugRenderer.camera.unproject(tmp);
        command.mousePosition.set(tmp.x, tmp.y);

        generateInputVector(directionVector);
        entity.position.add(directionVector.nor().scl(100 * delta));
        command.position.set(entity.position);

        World.currentInstance().addCommandToQueue(command);

        command = new Command();
        command.entityId = entityId;
        directionVector.set(0,0);
    }

    private void generateInputVector(Vector2 dirVector) {
        if (up && !down) {
            dirVector.add(0, 1);
        } else if (down && !up) {
            dirVector.add(0, -1);
        }

        if(left && !right){
            dirVector.add(-1,0);
        }
        else if (right && !left){
            dirVector.add(1,0);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case com.badlogic.gdx.Input.Keys.A:
                left = true;
                break;
            case com.badlogic.gdx.Input.Keys.D:
                right = true;
                break;
            case com.badlogic.gdx.Input.Keys.W:
                up = true;
                break;
            case com.badlogic.gdx.Input.Keys.S:
                down = true;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case com.badlogic.gdx.Input.Keys.A:
                left = false;
                break;
            case com.badlogic.gdx.Input.Keys.D:
                right = false;
                break;
            case com.badlogic.gdx.Input.Keys.W:
                up = false;
                break;
            case com.badlogic.gdx.Input.Keys.S:
                down = false;
                break;
            default:
                break;
        }
        return false;
    }
}
