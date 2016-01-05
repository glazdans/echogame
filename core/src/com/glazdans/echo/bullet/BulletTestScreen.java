package com.glazdans.echo.bullet;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.GdxGame;
import com.glazdans.echo.bullet.input.PlayerController;

import java.util.ArrayList;

public class BulletTestScreen implements Screen {

    final static short GROUND_FLAG = 1<<8;
    final static short OBJECT_FLAG = 1<<9;
    final static short ALL_FLAG = -1;

    private static Vector3 tmp = new Vector3();
    private static Vector3 tmp2 = new Vector3();
    private static Vector3 tmp3 = new Vector3();
    private static Vector3 tmp4 = new Vector3();
    private static Vector3 tmp5 = new Vector3();

    public static Array<Projectile> projectiles = new Array<>();

    class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded(btManifoldPoint cp, btCollisionObject colObj0, int partId0, int index0, btCollisionObject colObj1, int partId1, int index1) {
            GameObject a = getEntity(colObj0);
            GameObject b = getEntity(colObj1);
            if(a == null || b == null){
               return false;
            }
            if (a != null || b != null) {
                // the distance between the point on body A that collided and the point on body B that collided
                float dist = cp.getDistance();
                // the normal vector of the collision
                cp.getNormalWorldOnB(norm);
                cp.getPositionWorldOnA(worldA);
                cp.getPositionWorldOnA(worldB);
                if (a != null) {
                    entityCollision(a, norm, worldA, worldB, dist, b);
                }
                // for object b, we must reverse the normal direction
                if (b != null) {
                    entityCollision(b, norm.scl(-1f), worldB, worldA, dist, a);
                }
            }
            return true;
        }
        private GameObject getEntity(btCollisionObject obj) {
            if (obj.userData instanceof GameObject) return (GameObject) obj.userData;
            return null;
        }

        private Vector3 norm = new Vector3();
        private Vector3 worldA = new Vector3();
        private Vector3 worldB = new Vector3();

        /** this is where the magic happens! */
        private void entityCollision(GameObject ent, Vector3 normal, Vector3 myPoint, Vector3 otherPoint, float dist, GameObject otherEntity) {
            if (Math.abs(dist) < 0.01f) return; // ignoring tiny collisions seems to help stability?

            // Change Position
            tmp.set(myPoint).sub(otherPoint).nor().add(normal); // direction of vel + normal
            tmp.nor();
            Vector3 posDelta = tmp3.set(tmp).scl(dist);
            ent.addCollisionPositionChange(posDelta);

            // Change Velocity
            // slide along the faces of geometry, but bounce off other entities
            Vector3 vel = ent.velocity;
            boolean bounce = otherEntity != null;
            if (!bounce) {
                // slide along the wall
                // TODO don't slide "up" vertical inclines of a certain steepness
                projectVectorOntoPlane(vel, normal);
            } else {
                // bounce off of other entities
				/*float speed = vel.len();
				tmp4.set(vel).nor(); // entity unit velocity
				float dotScalar = tmp2.set(tmp4).dot(norm);
				tmp3.set(norm).scl(-2f * dotScalar);
				tmp2.set(tmp4).add(tmp3).nor();
				tmp2.scl(speed);
				// don't bounce up or down
				tmp2.y = 0f;
				ent.adjustVelocity(tmp2);
				otherEntity.adjustVelocity(tmp2.scl(-1f));*/
            }
        }

    }
    private static void projectVectorOntoPlane(Vector3 vec, Vector3 planeNorm) {
        // Formula: vector v1_projected = v1 - Dot(v1, n) * n;
        tmp2.set(planeNorm).scl(tmp.set(vec).dot(planeNorm));
        vec.sub(tmp2);
    }

    @Deprecated // USELESS CODE - DELETE
    static class MyMotionState extends btMotionState{
        Matrix4 transform;

        @Override
        public void getWorldTransform(Matrix4 worldTrans) {
            worldTrans.set(transform);
        }

        @Override
        public void setWorldTransform(Matrix4 worldTrans) {
            transform.set(worldTrans);
        }
    }

    final GdxGame gdxGame;
    Array<GameObject> gameObjects;
    DebugDrawer debugDrawer;

    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;
    MyContactListener contactListener;
    btBroadphaseInterface broadphase;

    public static btCollisionWorld collisionWorld;

    GameObject cameraObject;
    PlayerController playerController;
    InputMultiplexer inputMultiplexer;


    private PerspectiveCamera camera;

    private float cameraHeight = 18;

    public BulletTestScreen(GdxGame game){
        this.gdxGame = game;
        Bullet.init();
        gameObjects = new Array<>();
        camera = new PerspectiveCamera(60,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(new Vector3(0 ,35,-1));
        camera.lookAt(0,0,0);

        initBullet();
        playerController = new PlayerController(cameraObject,camera);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void initBullet(){
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher,broadphase,collisionConfig);

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        //dynamicsWorld.setDebugDrawer(debugDrawer);
        collisionWorld.setDebugDrawer(debugDrawer);
        contactListener = new MyContactListener();
        createObjects();
    }
    private void createObjects(){
        btBoxShape box = new btBoxShape(new Vector3(10,1,10));
        btCollisionShape ground = new btStaticPlaneShape(new Vector3(0,1,0),1);
        Vector3 localInertia = new Vector3();
        float mass = 0f;
        if(mass >0)
            box.calculateLocalInertia(mass,localInertia);
        btRigidBody.btRigidBodyConstructionInfo constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass,null,box,localInertia);
        GameObject gameObject = new GameObject(constructionInfo,false);
        gameObject.rigidBody.userData = gameObject;
        collisionWorld.addCollisionObject(gameObject.rigidBody); // TODO MAKE EVERYTHING COLLISION OBJECTS;
        gameObjects.add(gameObject);

        // WALL
        btCollisionShape wall = new btBoxShape(new Vector3(10,10,1));
        localInertia = new Vector3();
        mass = 0f;
        if(mass >0)
            box.calculateLocalInertia(mass,localInertia);
        constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass,null,wall,localInertia);
        gameObject = new GameObject(constructionInfo,false);
        gameObject.rigidBody.userData = gameObject;
        gameObject.rigidBody.setWorldTransform(new Matrix4().translate(0,9,11));
        collisionWorld.addCollisionObject(gameObject.rigidBody);
        gameObjects.add(gameObject);


        // RANDOM BOX
        box = new btBoxShape(new Vector3(1,1,1));
        localInertia = new Vector3();
        mass = 10f;
        if(mass >0)
            box.calculateLocalInertia(mass,localInertia);
        constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass,null,box,localInertia);
        gameObject = new GameObject(constructionInfo,true);
        gameObject.rigidBody.userData = gameObject;
        gameObject.rigidBody.setWorldTransform(new Matrix4().translate(new Vector3(0,5,0)));
        collisionWorld.addCollisionObject(gameObject.rigidBody);
        gameObjects.add(gameObject);

        // Player init
        btCapsuleShape capsuleShape = new btCapsuleShape(0.5f,3f);
        localInertia = new Vector3();
        mass = 400f;
        if(mass >0)
            capsuleShape.calculateLocalInertia(mass,localInertia);
        constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass,null,capsuleShape,localInertia);
        gameObject = new GameObject(constructionInfo,true);
        gameObject.rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
        gameObject.rigidBody.setWorldTransform(new Matrix4().setTranslation(0,5,0));
        gameObject.rigidBody.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        gameObject.rigidBody.userData = gameObject;
        collisionWorld.addCollisionObject(gameObject.rigidBody);

        gameObjects.add(gameObject);
        cameraObject = gameObject;
        cameraObject.rigidBody.userData = cameraObject;
    }

    @Override
    public void show() {

    }
    float minFrameRate = 1/30f;

    @Override
    public void render(float delta) {
        if(delta>minFrameRate){
            delta = minFrameRate;
        }
        playerController.updateMovement(collisionWorld);

        for (Projectile projectile : projectiles) {
            projectile.update(delta);
        }
        collisionWorld.performDiscreteCollisionDetection();
        cameraObject.update(delta,collisionWorld);

        Vector3 cameraObjectVector = cameraObject.transform.getTranslation(new Vector3());
        camera.position.set(cameraObjectVector.x,cameraHeight,cameraObjectVector.z);
        camera.lookAt(cameraObject.position);
        camera.update();



        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            new Boolean("false");
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.PLUS)){
            cameraHeight--;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.MINUS)){
            cameraHeight++;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            gdxGame.setScreen(new BulletTestScreen(gdxGame));
        }

        debugDrawer.begin(camera);
        collisionWorld.debugDrawWorld();
        debugDrawer.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        debugDrawer.dispose();
        contactListener.dispose();
        collisionWorld.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();
    }
}
