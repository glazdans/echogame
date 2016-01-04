package com.glazdans.echo.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class GameObject {
    public btRigidBody rigidBody;
    public Matrix4 transform;

    public Quaternion rotation;
    public Vector3 position;

    public Vector3 acceleration;
    public Vector3 velocity;

    private float movementSpeed = 50;
    public boolean isGrounded;

    final boolean isDynamic;

    private Vector3 collisionPositionChanges;
    int collisionPositionChangeCount;


    public GameObject(btRigidBody.btRigidBodyConstructionInfo constructionInfo, boolean isDynamic) {

        rigidBody = new btRigidBody(constructionInfo);
        transform = new Matrix4(rigidBody.getWorldTransform());
        transform.translate(0,10f,0);
        acceleration = new Vector3();
        velocity = new Vector3();
        collisionPositionChanges = new Vector3();
        this.isDynamic = isDynamic;
        transform.rotate(Vector3.Y,20);

        position = new Vector3();
        rotation = new Quaternion();
        position.y = 20;
    }

    public void addCollisionPositionChange(Vector3 posDelta) {
        collisionPositionChangeCount++;
        collisionPositionChanges.add(posDelta);
    }

    public void updateAcceleration(Vector3 direction){
        direction.nor();
        acceleration.set(direction.scl(movementSpeed));
    }
    public void setRotation(float angleY){
        //rotation.setEulerAngles(0,angleY,0);
    }
    private static Vector3 tmp = new Vector3();
    public void update(float delta,btCollisionWorld world){
        handleCollisions();

        btCollisionObject  collisionObject = rayTest(world);
        if(collisionObject != null) {
            isGrounded = true;
        } else {
            isGrounded = false;
        }

        if(!isGrounded){
            acceleration.add(0, -18, 0);
        }else{
            acceleration.y = 0;
            velocity.y = 0;
            position.y = 0;
        }

        tmp.set(acceleration);
        tmp.scl(delta);
        velocity.add(tmp);
        tmp.set(velocity);
        tmp.scl(delta);
        velocity = velocity.scl(0.95f);
        position.add(velocity);
        transform.set(position,rotation);
        rigidBody.setWorldTransform(transform);

    }

    public void handleCollisions() {
        if (collisionPositionChangeCount > 0) {
            collisionPositionChanges.scl(1f / collisionPositionChangeCount);
            collisionPositionChanges.scl(-1f); // subtraction
            adjustPosition(collisionPositionChanges);
            collisionPositionChangeCount = 0;
            collisionPositionChanges.setZero();
        }
    }

    private void adjustPosition(Vector3 change){
        //transform.translate(change);
        position.add(change);
    }

    private static final Vector3 rayFrom = new Vector3();
    private static final Vector3 rayTo = new Vector3();
    private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);

    public btCollisionObject rayTest(btCollisionWorld collisionWorld) {
        rayFrom.set(rigidBody.getWorldTransform().getTranslation(new Vector3()));
        // 50 meters max from the origin
        rayTo.set(rigidBody.getWorldTransform().getTranslation(new Vector3()).add(new Vector3(0, -3/2f-0.5f, 0))); // -3/2f-0.5f

        // we reuse the ClosestRayResultCallback, thus we need to reset its
        // values
        callback.setCollisionObject(null);
        callback.setClosestHitFraction(10);
        callback.setRayFromWorld(rayFrom);
        callback.setRayToWorld(rayTo);

        collisionWorld.rayTest(rayFrom, rayTo, callback);

        if (callback.hasHit()) {
            //Gdx.app.log("GameObject rayTest ","callback");
            return callback.getCollisionObject();
        }

        return null;
    }
}
