package com.glazdans.echo.events;

import com.badlogic.gdx.math.Vector3;

public class ActionEvent extends Event { // RENAME TO GENERIC UPDATE?
    public int id;
    public Vector3 direction;
    public boolean shoot;

    public float degree;

    public ActionEvent(){
        eventType = EventType.ActionEvent;
        direction = new Vector3();
    }
}
