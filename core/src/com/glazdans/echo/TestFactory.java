package com.glazdans.echo;

import com.badlogic.gdx.utils.Array;

public class TestFactory {

    public static void createTestLevel(Physics physics){
        Array<Terrain> terrains = new Array<>();
        for(int i =0; i <10;i++){
            Terrain t = new Terrain();
            t.position.set(16*i,0);
            terrains.add(t);
        }

        for(int i =0; i <10;i++){
            Terrain t = new Terrain();
            t.position.set(16*9,16*i);
            terrains.add(t);
        }
        for(int i =0; i <10;i++){
            Terrain t = new Terrain();
            t.position.set(0,16*i);
            terrains.add(t);
        }
        for(int i =0; i <10;i++){
            Terrain t = new Terrain();
            t.position.set(16*i,16*9);
            terrains.add(t);
        }
        physics.levelObjects.addAll(terrains);

    }
}
