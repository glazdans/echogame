package com.glazdans.echo.bullet;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class Projectile {
    btCollisionObject collisionObject;
    Vector3 position;
    Vector3 velocity;
    Vector3 acceleration;

    private Matrix4 transform;

    public Projectile(Vector3 startingPosition,Vector3 direction,float msSpeed,btCollisionWorld world){
        btCollisionShape sphere = new btSphereShape(0.5f);
        collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(sphere);
        collisionObject.userData = this;
        world.addCollisionObject(collisionObject);

        position = new Vector3(startingPosition);
        /*
        acceleration = new Vector3(direction);
        acceleration.nor().scl(msSpeed);*/

        velocity = new Vector3(direction);
        velocity.nor().scl(msSpeed);
        transform = new Matrix4();
        transform.setTranslation(position);
        collisionObject.setWorldTransform(transform);
    }

    private static Vector3 tmp = new Vector3();
    public void update(float delta){
        //tmp.set(acceleration);
        //tmp.scl(delta);
        //velocity.add(tmp);
        //velocity = velocity.scl(0.95f);
        tmp.set(velocity);
        tmp.scl(delta);
        position.add(tmp);

        transform.setTranslation(position);
        collisionObject.setWorldTransform(transform);
    }



}
