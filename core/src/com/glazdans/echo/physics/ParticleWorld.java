package com.glazdans.echo.physics;

import com.badlogic.gdx.utils.Array;

public class ParticleWorld {
    public Array<Particle> particles;

    public ParticleForceRegistry registry;

    public ParticleContactResolver resolver;

    public Array<ParticleContactGenerator> generators;

    Array<ParticleContact> contacts;

    int maxContacts;

    public ParticleWorld(int maxContacts, int iterations) {
        generators = new Array<>();
        contacts = new Array<>();
        particles = new Array<>();
        contacts = new Array<>();
        registry = new ParticleForceRegistry();
        resolver = new ParticleContactResolver(iterations);
    }

    public void startFrame() {
        for (Particle p : particles) {
            p.clearAccumulator();
        }
    }

    public int generateContacts() {
        contacts.clear();
        int limit = maxContacts;
        for (ParticleContactGenerator generator : generators) {
            int used = generator.addContact(contacts, limit);
            limit -= used;

            if (limit <= 0) break;
        }
        return maxContacts - limit;
    }

    public void integrate(float duration) {
        for (Particle p : particles) {
            p.integrate(duration);
        }
    }

    public void runPhysics(float duration) {
        registry.updateForces(duration);

        integrate(duration);

        generateContacts();

        resolver.resolveContacts(contacts, duration);
    }
}
