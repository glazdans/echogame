package com.glazdans.echo.bullet;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;

public class BulletTestScreen implements Screen {
    class MyContactListener extends ContactListener{
        @Override
        public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
            return super.onContactAdded(userValue0, partId0, index0, userValue1, partId1, index1);
        }
    }
    
    DebugDrawer debugDrawer;

    btCollisionConfiguration collisionConfig;
    btDispacher dispatcher;
    MyContactListener contactListener;
    btBroadphaseInterface broadphase;
    btCollisionWorld collisionWorld;


    private PerspectiveCamera camera;
    CameraInputController cameraInputController;

    public BulletTestScreen(){
        debugDrawer = new DebugDrawer();
        camera = new PerspectiveCamera();
        cameraInputController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraInputController);
        initBullet();
    }

    private void initBullet(){

        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadpahes = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher,broadphase,collisionConfig);
        contactListener = new MyContactListener();

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        debugDrawer.begin(camera);
        collisionWorld.setDebugDrawer(debugDrawer);
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
    }
}
