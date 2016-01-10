package com.glazdans.echo.physics;

import com.badlogic.gdx.utils.Array;

/**
 * Created by georgs.lazdans on 2015.11.04..
 */
public class ParticleForceRegistry {

    static class ParticleForceRegistration{
        public Particle particle;
        public ParticleForceGenerator forceGenerator;
    }

    Array<ParticleForceRegistration> registrations;

    public ParticleForceRegistry() {
        registrations = new Array<>();
    }

    public void add(Particle particle, ParticleForceGenerator forceGenerator){
        ParticleForceRegistration registration = new ParticleForceRegistration();
        registration.particle= particle;
        registration.forceGenerator= forceGenerator;
        registrations.add(registration);
    }
    public void remove(Particle particle, ParticleForceGenerator forceGenerator){
        ParticleForceRegistration registration = new ParticleForceRegistration();
        registration.particle= particle;
        registration.forceGenerator= forceGenerator;
        registrations.removeValue(registration,false);
    }

    public void clear(){
        registrations.clear();
    }
    public void updateForces(float duration){
        for(ParticleForceRegistration registration : registrations){
            registration.forceGenerator.updateForce(registration.particle,duration);
        }
    }
}
