package com.glazdans.echo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public class Entity {
    public int id;
    public Vector2 position;
    public Vector2 mousePosition;
    public Vector2 dimensions;

    public float restitution;
    public float mass;
    public Vector2 velocity;

    Vector2 newPosition;

    public Entity(float mass) {
        position = new Vector2();
        mousePosition = new Vector2();
        newPosition = new Vector2();
        dimensions = new Vector2(16,16);
        this.mass = mass;
        velocity = new Vector2();
    }

    public void setCommand(Command command){
        newPosition.set(command.position);
        mousePosition.set(command.mousePosition.sub(command.position));
        mousePosition.nor().scl(100f);
    }

    public void update(){
        //position.set(newPosition);

    }
}
