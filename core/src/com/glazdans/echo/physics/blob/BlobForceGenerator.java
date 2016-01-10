package com.glazdans.echo.physics.blob;

import com.badlogic.gdx.math.Vector3;
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

    private static Vector3 tmp = new Vector3();
    @Override
    public void updateForce(Particle particle, float duration) {
        int joinCount = 0;
        for(Particle p : particles){
            if(p.equals(particle))continue;
            Vector3 separation = new Vector3(p.velocity).sub(particle.position);
            separation.z = 0;

            float distance = separation.len();

            if(distance < minNaturalDistance){
                distance = 1.0f - distance/ minNaturalDistance;
                particle.addForce(tmp.set(separation).nor().scl((1.0f - distance) * maxRepulsion * -1.0f));
                joinCount++;
            }
             else if(distance > maxNaturalDistance && distance < maxDistance){
                distance =
                        (distance - maxNaturalDistance) /
                                (maxDistance - maxNaturalDistance);
                particle.addForce(tmp.set(separation).nor().scl(distance*maxAttraction));
                joinCount++;
            }
        }
        if (particle == particles.get(0) && joinCount > 0 && maxFloat > 0){
            float force =  (joinCount / maxFloat) * floatHead;
            if (force > floatHead) force = floatHead;
            particle.addForce(new Vector3(0,force,0));
        }
    }
}
