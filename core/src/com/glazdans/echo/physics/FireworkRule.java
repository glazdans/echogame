package com.glazdans.echo.physics;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by georgs.lazdans on 2015.10.28..
 */
public class FireworkRule {
    int type;
    float minAge;
    float maxAge;
    Vector3 minVelocity;
    Vector3 maxVelocity;
    float damping;

    public static class Payload{
        int type;
        int count;
    }

    int payloadCount;

    Payload payload;

    public void create(Firework firework, Firework parent){

    }
}
