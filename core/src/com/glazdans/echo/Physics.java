package com.glazdans.echo;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Physics {
    Array<Terrain> levelObjects;
    public static int TILE_SIZE = 16;

    private static Physics currentInstance;
    public Physics(){
        levelObjects = new Array<>();
        currentInstance = this;
        TestFactory.createTestLevel(this);
    }

    public static Physics getCurrentInstance(){
        return currentInstance;
    }

    private static Rectangle tmp1 = new Rectangle();
    private static Rectangle tmp2 = new Rectangle();
    public static boolean areColliding(Entity e, Terrain t){
        tmp1.set(e.position.x,e.position.y,TILE_SIZE,TILE_SIZE);
        tmp2.set(t.position.x,t.position.y,TILE_SIZE,TILE_SIZE);
        return tmp1.overlaps(tmp2);
    }
}
