package com.glazdans.echo.physics;

/**
 * Created by georgs.lazdans on 2015.11.04..
 */
public interface ParticleForceGenerator {
    void updateForce(Particle particle, float duration);
}
