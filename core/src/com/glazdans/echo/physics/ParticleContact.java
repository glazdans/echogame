package com.glazdans.echo.physics;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by georgs.lazdans on 2015.11.04..
 */
public class ParticleContact {
    Particle p1;
    Particle p2;

    float restitution;
    Vector3 contactNormal;
// 110lpp
    private static Vector3 tmp = new Vector3();

    public void resolve(float duration){
        resolveVelocity(duration);
    }

    public float calculateSeparatingVelocity(){
        Vector3 relativeVelocity = new Vector3(p1.velocity);
        if(p2 != null){
            relativeVelocity.sub(p2.velocity);
        }
        return relativeVelocity.dot(contactNormal);
    }

    private void resolveVelocity(float duration){
        float separatingVelocity = calculateSeparatingVelocity();

        if(separatingVelocity > 0){
            return;
        }

        float newSepVelocity = -separatingVelocity * restitution;
        float deltaVelocity = newSepVelocity - separatingVelocity;
        float totalInverseMass = p1.inverseMass;
        if(p2 != null){
            totalInverseMass += p2.inverseMass;
        }

        if(totalInverseMass <= 0){
            return;
        }

        float impulse = deltaVelocity /totalInverseMass;
        tmp.set(contactNormal);
        Vector3 impulsePerIMass = tmp.scl(impulse);

        p1.velocity.set(p1.velocity.add(impulsePerIMass.scl(p1.inverseMass)));

        if(p2 != null){
            tmp.set(contactNormal);
            impulsePerIMass = tmp.scl(impulse);
            p2.velocity.set(p2.velocity.add(impulsePerIMass.scl(p2.inverseMass)));
        }
    }
}
