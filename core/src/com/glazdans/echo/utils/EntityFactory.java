package com.glazdans.echo.utils;

import com.artemis.World;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.glazdans.echo.bullet.GameObject;
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

        // Maybe not needed
        collisionObject.setActivationState(Collision.DISABLE_DEACTIVATION);
        collisionObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

        collisionObject.setCollisionShape(capsuleShape);
        component.setCollisionObject(collisionObject);

        collisionObject.userData = entity;

        Physics.getInstance().addDynamicObject(collisionObject);

        return entity;
    }

    public static int createStaticObject(btCollisionShape shape, World world){
        int entity = world.create();
        TransformComponent transformComponent = world.getMapper(TransformComponent.class).create(entity);

        PhysicsComponent component = world.getMapper(PhysicsComponent.class).create(entity);
        btCollisionObject collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(shape);
        component.setCollisionObject(collisionObject);

        collisionObject.userData = entity;

        Physics.addStaticObject(collisionObject);

        return entity;
    }
}
