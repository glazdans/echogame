package com.glazdans.echo.physics;

import com.badlogic.gdx.math.Vector2;
import com.glazdans.echo.Entity;

/**
 * Created by georgs.lazdans on 2015.10.17..
 */
@Deprecated
public class Manifold {
    public Entity a;
    public Entity b;
    public float penetration;
    public Vector2 normal;

    public Manifold(){
        normal = new Vector2();
    }
}
