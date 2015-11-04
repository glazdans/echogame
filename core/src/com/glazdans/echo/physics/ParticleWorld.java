package com.glazdans.echo.physics;

import com.badlogic.gdx.utils.Array;

public class ParticleWorld {
    Array<Particle> particles;

    ParticleForceRegistry registry;

    ParticleContactResolver resolver;

    Array<ParticleContactGenerator> generators;

    Array<ParticleContact> contacts;

    int maxContacts;

    public ParticleWorld(int maxContacts,int iterations){

    }
    public void startFrame(){
        for(Particle p : particles){
            p.clearAccumulator();
        }
    }

    public int generateContacts(){
        int limit = maxContacts;
        for(ParticleContactGenerator generator : generators){
            int used = generator.addContact(contacts,limit);
            limit -=used;

            if(limit<= 0) break;
        }
        return maxContacts - limit;
    }

    public void integrate(float duration){
        for(Particle p : particles){
            p.integrate(duration);
        }
    }

    void runPhysics(float duration){
        registry.updateForces(duration);

        integrate(duration);

        resolver.resolveContacts(contacts,duration);
    }
}
