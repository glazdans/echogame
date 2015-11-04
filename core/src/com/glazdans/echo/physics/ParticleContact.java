package com.glazdans.echo.physics;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by georgs.lazdans on 2015.11.04..
 */
public class ParticleContact {
    public Particle p1;
    public Particle p2;

    public float restitution;
    public Vector3 contactNormal;
    public float penetration;
    // 110 lpp
    private static Vector3 tmp = new Vector3();

    public void resolve(float duration) {
        resolveVelocity(duration);
        resolveInterpenetration(duration);
    }

    public float calculateSeparatingVelocity() {
        Vector3 relativeVelocity = new Vector3(p1.velocity);
        if (p2 != null) {
            relativeVelocity.sub(p2.velocity);
        }
        return relativeVelocity.dot(contactNormal);
    }

    private void resolveVelocity(float duration) {
        float separatingVelocity = calculateSeparatingVelocity();

        if (separatingVelocity > 0) {
            return;
        }

        float newSepVelocity = -separatingVelocity * restitution;

        Vector3 accCausedVelocity = new Vector3(p1.acceleration);
        if(p2 != null){
            accCausedVelocity.sub(p2.velocity);
        }
        float accCausedSepVelocity = accCausedVelocity.dot(contactNormal) * duration;

        if (accCausedSepVelocity < 0)
        {
            newSepVelocity += restitution * accCausedSepVelocity;
            if (newSepVelocity < 0) newSepVelocity = 0;
        }


        float deltaVelocity = newSepVelocity - separatingVelocity;

        float totalInverseMass = p1.inverseMass;
        if (p2 != null) {
            totalInverseMass += p2.inverseMass;
        }

        if (totalInverseMass <= 0) {
            return;
        }

        float impulse = deltaVelocity / totalInverseMass;
        tmp.set(contactNormal);
        Vector3 impulsePerIMass = tmp.scl(impulse);

        p1.velocity.set(p1.velocity.add(impulsePerIMass.scl(p1.inverseMass)));

        if (p2 != null) {
            tmp.set(contactNormal);
            impulsePerIMass = tmp.scl(impulse);
            p2.velocity.set(p2.velocity.add(impulsePerIMass.scl(p2.inverseMass)));
        }
    }

    private void resolveInterpenetration(float duration) {
        if(penetration <=0)return;

        float totalInverseMass = p1.inverseMass;
        if(p2 != null){
            totalInverseMass += p2.inverseMass;
        }

        if(totalInverseMass <= 0) return;
        Vector3 movePerIMass = new Vector3(contactNormal).scl(-penetration/totalInverseMass);

        tmp.set(movePerIMass).scl(p1.inverseMass);
        p1.position.add(tmp);

        if(p2 != null){
            tmp.set(movePerIMass).scl(p2.inverseMass);
            p2.position.add(tmp);
        }

    }
}
