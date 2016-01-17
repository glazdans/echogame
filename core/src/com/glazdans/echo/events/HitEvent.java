package com.glazdans.echo.events;

import com.badlogic.gdx.math.Vector3;

public class HitEvent extends Event {
    public int entityShooterId;
    public int entityShotId;

    public Vector3 hitPoint;
    public Vector3 normal;

    public HitEvent(){
        eventType = EventType.HitEvent;

        hitPoint = new Vector3();
        normal = new Vector3();
    }
}
