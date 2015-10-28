package com.glazdans.echo.physics;

/**
 * Created by georgs.lazdans on 2015.10.28..
 */
public class Firework extends Particle {
    float age;
    int type;

    public void update(float duration){
        integrate(duration);

        age -= duration;
    }
}
