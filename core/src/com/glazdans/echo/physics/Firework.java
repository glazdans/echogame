package com.glazdans.echo.physics;

import com.badlogic.gdx.utils.Pool;

/**
 * Created by georgs.lazdans on 2015.10.28..
 */
public class Firework extends Particle implements Pool.Poolable{
    float age;
    int type;

    public Firework(){
        super();
    }
    public void update(float duration){
        integrate(duration);

        age -= duration;
    }

    @Override
    public void reset() {
        age = 0;
        type = 0;
        forceAccum.set(0,0,0);
        damping = .9f;
        acceleration.set(0,0,0);
        velocity.set(0,0,0);
        position.set(0,0,0);
        inverseMass = 1;
    }
}
