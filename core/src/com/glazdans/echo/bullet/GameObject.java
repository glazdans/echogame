package com.glazdans.echo.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class GameObject {
    public btRigidBody rigidBody;
    public Matrix4 transform;
    public BulletTestScreen.MyMotionState motionState;

    public boolean isGrounded;

    public GameObject(btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        rigidBody = new btRigidBody(constructionInfo);
        //motionState = new MyMotionState();
        //motionState.transform = transform;
        //rigidBody.setMotionState(motionState);
        transform = new Matrix4(rigidBody.getWorldTransform());
        transform.translate(0,15f,0);
    }

    public void update(float delta,btDynamicsWorld world){
        btCollisionObject  collisionObject = rayTest(world);
        if(collisionObject != null) {
            isGrounded = true;
        } else {
            isGrounded = false;
        }

        if(!isGrounded){
            rigidBody.setWorldTransform(transform.translate(0,-9*delta,0));
        }

    }

    private static final Vector3 rayFrom = new Vector3();
    private static final Vector3 rayTo = new Vector3();
    private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);

    public btCollisionObject rayTest(btCollisionWorld collisionWorld) {
        rayFrom.set(rigidBody.getWorldTransform().getTranslation(new Vector3()));
        // 50 meters max from the origin
        rayTo.set(rigidBody.getWorldTransform().getTranslation(new Vector3()).add(new Vector3(0, -3/2f-0.5f, 0)));

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
