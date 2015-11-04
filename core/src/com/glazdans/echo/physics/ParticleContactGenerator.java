package com.glazdans.echo.physics;

import com.badlogic.gdx.utils.Array;

public interface ParticleContactGenerator {
    int addContact(Array<ParticleContact> contact,int limit);
}
