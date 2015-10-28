package com.glazdans.echo.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by georgs.lazdans on 2015.10.28..
 */
public class Particle {
    Vector3 position;
    Vector3 velocity;
    Vector3 acceleration;

    Vector3 forceAccum;

    float damping;

    float inverseMass;


    private static Vector3 tmp = new Vector3();

    public void update(float time){
        tmp.set(velocity).scl(time);
        position.add(tmp);
        tmp.set(acceleration).scl(time*time*0.5f);
        position.add(tmp);
    }

    void integrate(float duration){
        // Update linear position
        tmp.set(velocity).scl(duration);
        position.add(tmp);

        // acceleration
        // Work out the acceleration from the force.
        Vector3 resultingAcc = new Vector3(acceleration);
        tmp.set(forceAccum).scl(inverseMass);
        resultingAcc.add(tmp);

        // Update linear velocity from the acceleration.

        velocity.add(resultingAcc.scl(duration));

        // Impose drag.
        velocity.scl((float) Math.pow(damping,duration));

    }
}
