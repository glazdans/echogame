package com.glazdans.echo.bullet;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;

public class GameObjectFactory {
    // TODO remove rigid body info
    public static void createObjects(btCollisionWorld collisionWorld,Array<GameObject> gameObjects) {
        btBoxShape box = new btBoxShape(new Vector3(10, 1, 10));
        btCollisionShape ground = new btStaticPlaneShape(new Vector3(0, 1, 0), 1);
        Vector3 localInertia = new Vector3();
        float mass = 0f;
        if (mass > 0)
            box.calculateLocalInertia(mass, localInertia);
        btRigidBody.btRigidBodyConstructionInfo constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, box, localInertia);
        GameObject gameObject = new GameObject(box, false);
        gameObject.rigidBody.userData = gameObject;
        collisionWorld.addCollisionObject(gameObject.rigidBody); // TODO MAKE EVERYTHING COLLISION OBJECTS;
        gameObjects.add(gameObject);

        // WALL
        btCollisionShape wall = new btBoxShape(new Vector3(10, 10, 1));
        localInertia = new Vector3();
        mass = 0f;
        if (mass > 0)
            box.calculateLocalInertia(mass, localInertia);
        constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, wall, localInertia);
        gameObject = new GameObject(wall, false);
        gameObject.rigidBody.userData = gameObject;
        gameObject.rigidBody.setWorldTransform(new Matrix4().translate(0, 9, 11));
        collisionWorld.addCollisionObject(gameObject.rigidBody);
        gameObjects.add(gameObject);


        // RANDOM BOX
        box = new btBoxShape(new Vector3(1, 1, 1));
        localInertia = new Vector3();
        mass = 10f;
        if (mass > 0)
            box.calculateLocalInertia(mass, localInertia);
        constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, box, localInertia);
        gameObject = new GameObject(box, true);
        gameObject.rigidBody.userData = gameObject;
        gameObject.rigidBody.setWorldTransform(new Matrix4().translate(new Vector3(0, 5, 0)));
        collisionWorld.addCollisionObject(gameObject.rigidBody);
        gameObjects.add(gameObject);
    }
    public static GameObject getPlayer(btCollisionWorld collisionWorld,Array<GameObject> gameObjects){
        // Player init
        btCapsuleShape capsuleShape = new btCapsuleShape(0.5f,3f);
        Vector3 localInertia = new Vector3();
        float mass = 400f;
        if(mass >0)
            capsuleShape.calculateLocalInertia(mass,localInertia);
        btRigidBody.btRigidBodyConstructionInfo constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass,null,capsuleShape,localInertia);
        GameObject gameObject = new GameObject(capsuleShape,true);
        gameObject.rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
        gameObject.rigidBody.setWorldTransform(new Matrix4().setTranslation(0,5,0));
        gameObject.rigidBody.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        gameObject.rigidBody.userData = gameObject;
        collisionWorld.addCollisionObject(gameObject.rigidBody);

        gameObjects.add(gameObject);
        return gameObject;
    }
}
