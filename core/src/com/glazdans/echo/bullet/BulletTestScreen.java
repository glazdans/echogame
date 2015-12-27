package com.glazdans.echo.bullet;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.glazdans.echo.GdxGame;
import com.glazdans.echo.bullet.input.PlayerController;

import java.util.ArrayList;

public class BulletTestScreen implements Screen {

    final static short GROUND_FLAG = 1<<8;
    final static short OBJECT_FLAG = 1<<9;
    final static short ALL_FLAG = -1;

    class MyContactListener extends ContactListener {

        @Override
        public boolean onContactAdded(btCollisionObject colObj0, int partId0, int index0, btCollisionObject colObj1, int partId1, int index1) {
            if (colObj0.userData.equals(cameraObject)) {
                cameraObject.isGrounded = true;
            } else if (colObj1.userData.equals(cameraObject)) {
                cameraObject.isGrounded = true;
            }
            Gdx.app.log("OnContactAded", ": contacts!");
            return true;
        }
    }
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

    btDynamicsWorld dynamicsWorld;
    btConstraintSolver constraintSolver;

    GameObject cameraObject;
    PlayerController playerController;
    InputMultiplexer inputMultiplexer;


    private PerspectiveCamera camera;
    CameraInputController cameraInputController;

    public BulletTestScreen(GdxGame game){
        this.gdxGame = game;
        Bullet.init();
        gameObjects = new Array<>();
        camera = new PerspectiveCamera(80,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(new Vector3(15 ,10,0));
        cameraInputController = new CameraInputController(camera);

        initBullet();
        playerController = new PlayerController(cameraObject,camera);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(cameraInputController);
        inputMultiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void initBullet(){
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher,broadphase,constraintSolver,collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0,-9f,0));

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        dynamicsWorld.setDebugDrawer(debugDrawer);
        contactListener = new MyContactListener();
        contactListener.enable();
        createObjects();
    }
    private void createObjects(){
        btBoxShape box = new btBoxShape(new Vector3(10,1,10));
        Vector3 localInertia = new Vector3();
        float mass = 0f;
        if(mass >0)
            box.calculateLocalInertia(mass,localInertia);
        btRigidBody.btRigidBodyConstructionInfo constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass,null,box,localInertia);
        GameObject gameObject = new GameObject(constructionInfo);
        gameObject.rigidBody.setCollisionFlags(btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);
        gameObject.rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
        dynamicsWorld.addRigidBody(gameObject.rigidBody, GROUND_FLAG ,ALL_FLAG);
        gameObjects.add(gameObject);

        // Player init
        btCapsuleShape capsuleShape = new btCapsuleShape(0.5f,3f);
        localInertia = new Vector3();
        mass = 4f;
        if(mass >0)
            capsuleShape.calculateLocalInertia(mass,localInertia);
        constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass,null,capsuleShape,localInertia);
        gameObject = new GameObject(constructionInfo);
        gameObject.rigidBody.setCollisionFlags(btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        gameObject.rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
        gameObject.rigidBody.setWorldTransform(new Matrix4().setTranslation(0,10,0));
        dynamicsWorld.addRigidBody(gameObject.rigidBody, OBJECT_FLAG,ALL_FLAG);
        gameObjects.add(gameObject);
        cameraObject = gameObject;
        cameraObject.rigidBody.userData = cameraObject;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        cameraObject.update(delta,dynamicsWorld);
        dynamicsWorld.stepSimulation(delta, 5, 1 / 60f);
        camera.lookAt(cameraObject.rigidBody.getWorldTransform().getTranslation(new Vector3()));
        camera.update();
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            new Boolean("false");
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            gdxGame.setScreen(new BulletTestScreen(gdxGame));
        }

        debugDrawer.begin(camera);
        dynamicsWorld.debugDrawWorld();
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
    }
}
