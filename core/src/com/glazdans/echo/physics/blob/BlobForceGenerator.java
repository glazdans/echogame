package com.glazdans.echo.physics.blob;

import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.physics.Particle;
import com.glazdans.echo.physics.ParticleForceGenerator;

public class BlobForceGenerator implements ParticleForceGenerator {
    Array<Particle> particles;
    float maxRepulsion;
    float maxAttraction;
    float minNaturalDistance, maxNaturalDistance;
    float floatHead;

    int maxFloat;

    float maxDistance;
    @Override
    public void updateForce(Particle particle, float duration) {
        for(Particle p : particles){
            if(p.equals(particle))continue;

        }
    }
}
