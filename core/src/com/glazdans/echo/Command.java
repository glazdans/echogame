package com.glazdans.echo;

import com.badlogic.gdx.math.Vector2;

public class Command {
    int entityId;
    Vector2 position;
    Vector2 mousePosition;

    public Command() {
        position = new Vector2();
        mousePosition = new Vector2();
    }

    public void clearCommand(){
        position.set(0,0);
        mousePosition.set(0,0);
    }
}
