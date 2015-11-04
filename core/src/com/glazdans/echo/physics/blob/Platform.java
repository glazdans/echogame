package com.glazdans.echo.physics.blob;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.physics.Particle;
import com.glazdans.echo.physics.ParticleContact;
import com.glazdans.echo.physics.ParticleContactGenerator;

public class Platform implements ParticleContactGenerator {

    static float restitution = 0f;
    Vector3 start;
    Vector3 end;
    Array<Particle> particles;

    @Override
    public int addContact(Array<ParticleContact> contacts, int limit) {
        int used = 0;
        for (Particle p : particles) {
            Vector3 toParticle = new Vector3(p.position).sub(start);
            Vector3 lineDirection = new Vector3(end).sub(start);
            float projected = toParticle.dot(lineDirection);
            float platformSqLength = lineDirection.len2();

            if (projected <= 0) {
                if (toParticle.len2() < BlobScreen.BLOB_RADIUS * BlobScreen.BLOB_RADIUS) {
                    ParticleContact contact = new ParticleContact();
                    contact.contactNormal = new Vector3(toParticle.nor());
                    contact.contactNormal.z = 0;
                    contact.restitution = restitution;
                    contact.p1 = p;
                    contact.p2 = null;
                    contact.penetration = BlobScreen.BLOB_RADIUS - toParticle.len();
                    contacts.add(contact);
                    used++;
                }
            } else if (projected >= platformSqLength) {
                toParticle = new Vector3(p.position).sub(end);
                if (toParticle.len2() < BlobScreen.BLOB_RADIUS * BlobScreen.BLOB_RADIUS) {
                    ParticleContact contact = new ParticleContact();
                    contact.contactNormal = new Vector3(toParticle.nor());
                    contact.contactNormal.z = 0;
                    contact.restitution = restitution;
                    contact.p1 = p;
                    contact.p2 = null;
                    contact.penetration = BlobScreen.BLOB_RADIUS - toParticle.len();
                    contacts.add(contact);
                    used++;
                }
            } else {
                float distanceToPlatform = toParticle.len2() - projected * projected / platformSqLength;
                if(distanceToPlatform < BlobScreen.BLOB_RADIUS * BlobScreen.BLOB_RADIUS){
                    Vector3 closestPoint = new Vector3(lineDirection).scl(projected/platformSqLength).add(start);
                    ParticleContact contact = new ParticleContact();
                    contact.contactNormal = new Vector3(p.position).sub(closestPoint).nor();
                    contact.contactNormal.z = 0;
                    contact.restitution = restitution;
                    contact.p1 = p;
                    contact.p2 = null;
                    contact.penetration = BlobScreen.BLOB_RADIUS - (float)Math.sqrt(distanceToPlatform);
                    contacts.add(contact);
                    used++;
                }
            }
        }
        return used;
    }
}
