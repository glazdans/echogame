package com.glazdans.echo.component;

import com.artemis.Component;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public class PhysicsComponent extends Component {
    public btCollisionObject collisionObject;

    public void setCollisionObject(btCollisionObject btCollisionObject){
        collisionObject = btCollisionObject;
    }
}
