package com.glazdans.echo.utils;

import com.artemis.World;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.glazdans.echo.bullet.Physics;
import com.glazdans.echo.component.*;

public class EntityFactory {

    public static int playerEntity(World world){
        int entity = world.create();
        world.getMapper(PlayerComponent.class).create(entity);
        TransformComponent transformComponent = world.getMapper(TransformComponent.class).create(entity);
        transformComponent.position.set(0,15,0);
        world.getMapper(MovementComponent.class).create(entity);

        PhysicsComponent component = world.getMapper(PhysicsComponent.class).create(entity);
        btCapsuleShape capsuleShape = new btCapsuleShape(0.5f,3f);
        btCollisionObject collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(capsuleShape);
        component.setCollisionObject(collisionObject);

        Physics.getInstance().addDynamicObject(collisionObject);

        return entity;
    }
}
