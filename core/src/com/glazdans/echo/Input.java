package com.glazdans.echo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class Input extends InputAdapter {
    private int entityId;
    private Command command;

    private Vector3 tmp = new Vector3();

    public Input(int entityId) {
        this.entityId = entityId;
        command = new Command();
        command.entityId = entityId;
    }

    public void updateInput(){
        tmp.set(Gdx.input.getX(),Gdx.input.getY(),0);
        tmp = DebugRenderer.camera.unproject(tmp);
        command.mousePosition.set(tmp.x,tmp.y);

        World.currentInstance().addCommandToQueue(command);

        command = new Command();
        command.entityId=entityId;
    }



    @Override
    public boolean keyDown(int keycode) {
        OrthographicCamera camera = DebugRenderer.camera;
        if(com.badlogic.gdx.Input.Keys.A == keycode){
            camera.translate(-20 ,0);
        }
        if(com.badlogic.gdx.Input.Keys.D == keycode){
            camera.translate(20 ,0);
        }
        if(com.badlogic.gdx.Input.Keys.S == keycode){
            camera.translate(0,-20);
        }
        if(com.badlogic.gdx.Input.Keys.W == keycode){
            camera.translate(0,20);
        }
        return false;
    }
}
