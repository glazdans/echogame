package com.glazdans.echo.physics;

import com.badlogic.gdx.utils.Array;

public class ParticleContactResolver {
    int iterations;
    int iterationsUsed;

    public ParticleContactResolver(int iterations){
        this.iterations = iterations;
    }

    public void setIterations(int iterations){
        this.iterations=iterations;
    }

    public void resolveContacts(Array<ParticleContact> contacts,float duration){
        iterationsUsed = 0;
        while(iterationsUsed < iterations){
            float max = 0;
            int maxIndex = contacts.size;
            for(int i = 0; i < contacts.size; i++){
                float sepVel = contacts.get(i).calculateSeparatingVelocity();
                if(sepVel<max){
                    max = sepVel;
                    maxIndex = i;
                }
                contacts.get(maxIndex).resolve(duration);
                iterationsUsed++;
            }
        }
    }
}
