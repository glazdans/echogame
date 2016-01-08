package com.glazdans.echo.utils;

import com.artemis.World;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.glazdans.echo.components.*;

public class EntityFactory {

    public static int playerEntity(World world){
        int entity = world.create();
        world.getMapper(PlayerComponent.class).create(entity);
        world.getMapper(TransformComponent.class).create(entity);
        world.getMapper(MovementComponent.class).create(entity);
        world.getMapper(InputComponent.class).create(entity);

        PhysicsComponent component = world.getMapper(PhysicsComponent.class).create(entity);
        btCapsuleShape capsuleShape = new btCapsuleShape(0.5f,3f);
        btCollisionObject collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(capsuleShape);
        component.setCollisionObject(collisionObject);

        return entity;
    }
}
