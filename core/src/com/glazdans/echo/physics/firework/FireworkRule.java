package com.glazdans.echo.physics.firework;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.glazdans.echo.physics.Particle;
import com.glazdans.echo.physics.firework.Firework;

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

    public static class Payload {
        int type;
        int count;
    }

    int payloadCount;

    Payload payload;

    private static Vector3 tmp = new Vector3();

    public Firework create(Firework firework, Firework parent) {
        firework.type = type;
        firework.age = MathUtils.random(minAge, maxAge);

        if (parent != null) {
            firework.velocity.set(parent.velocity);
            firework.position.set(parent.position);
        }
        tmp.set(MathUtils.random(minVelocity.x, maxVelocity.x), MathUtils.random(minVelocity.y, maxVelocity.y), MathUtils.random(minVelocity.z, maxVelocity.z));
        firework.velocity.add(tmp);

        firework.setMass(1);
        firework.damping = damping;

        firework.acceleration.set(Particle.globalGravity);
        firework.forceAccum.set(0,0,0);

        return firework;
    }
}
