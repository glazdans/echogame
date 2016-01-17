package com.glazdans.echo.utils;

import com.artemis.World;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.glazdans.echo.bullet.Physics;
import com.glazdans.echo.component.*;
import com.glazdans.echo.effects.KnockbackEffect;

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

        AttackComponent attack = world.getMapper(AttackComponent.class).create(entity);
        WeaponFactory.defaultWeapon(attack);

        WeaponComponent weapon = world.getMapper(WeaponComponent.class).create(entity);
        KnockbackEffect knockbackEffect = new KnockbackEffect();
        knockbackEffect.knockbackStrength = 1f;
        weapon.weaponEffects.add(knockbackEffect);

        Physics.getInstance().addDynamicObject(collisionObject);
        Physics.getInstance().disposables.add(capsuleShape);
        return entity;
    }

    public static int createEnemyObject(World world,Vector3 position){
        int entity = world.create();

        world.getMapper(PlayerComponent.class).create(entity);
        TransformComponent transformComponent = world.getMapper(TransformComponent.class).create(entity);
        transformComponent.position.set(position);
        world.getMapper(MovementComponent.class).create(entity);

        PhysicsComponent component = world.getMapper(PhysicsComponent.class).create(entity);
        btCapsuleShape capsuleShape = new btCapsuleShape(0.5f,3f);
        btCollisionObject collisionObject = new btCollisionObject();

        // Maybe not needed
        //collisionObject.setActivationState(Collision.DISABLE_DEACTIVATION);
        collisionObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

        collisionObject.setCollisionShape(capsuleShape);
        component.setCollisionObject(collisionObject);

        collisionObject.userData = entity;


        Physics.getInstance().addDynamicObject(collisionObject);
        Physics.getInstance().disposables.add(capsuleShape);
        return 0;
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

        Physics.getInstance().disposables.add(shape);
        return entity;
    }
}
