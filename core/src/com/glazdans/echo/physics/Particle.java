package com.glazdans.echo.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by georgs.lazdans on 2015.10.28..
 */
public class Particle {
    public static final Vector3 globalGravity = new Vector3(0,-60,0);
    public Vector3 position;
    public Vector3 velocity;
    Vector3 acceleration;

    Vector3 forceAccum;

    float damping;

    float inverseMass;

    public Particle(){
        position = new Vector3();
        velocity = new Vector3();
        acceleration = new Vector3();
        forceAccum = new Vector3();
    }

    public void clearAccumulator(){
        forceAccum.set(0,0,0);
    }

    public void addForce(Vector3 force){
        forceAccum.add(force);
    }

    public void setMass(float mass){
        if(mass == 0){
            inverseMass = 0;
        }
        else{
            inverseMass = 1/mass;
        }
    }

    private static Vector3 tmp = new Vector3();

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

        // Clear the forces
        clearAccumulator();
    }
}
