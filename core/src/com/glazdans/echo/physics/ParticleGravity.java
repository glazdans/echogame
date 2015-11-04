package com.glazdans.echo.physics;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by georgs.lazdans on 2015.11.04..
 */
public class ParticleGravity implements ParticleForceGenerator {
    Vector3 gravity;

    public ParticleGravity(Vector3 gravity){
        this.gravity=gravity;
    }
    private static Vector3 tmp = new Vector3();
    @Override
    public void updateForce(Particle particle, float duration) {
        if(particle.inverseMass == 0){
            return;
        }
        tmp.set(gravity).scl(duration);
        particle.addForce(tmp);
    }
}
