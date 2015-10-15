package com.glazdans.echo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Entity {
    public int id;
    public Vector2 position;
    public Vector2 mousePosition;

    Vector2 newPosition;

    public Entity() {
        position = new Vector2();
        mousePosition = new Vector2();
        newPosition = new Vector2();
    }

    public void setCommand(Command command){
        newPosition.set(command.position);
        mousePosition.set(command.mousePosition.sub(command.position));
        mousePosition.nor().scl(30f);
        Gdx.app.debug("Entity command method",mousePosition.toString());
        Gdx.app.debug("Entity  command.mousePosition",command.mousePosition.toString());
        Gdx.app.debug("Entity command.position",command.position.toString());
    }

    public void update(){
        position.set(newPosition);

    }
}
