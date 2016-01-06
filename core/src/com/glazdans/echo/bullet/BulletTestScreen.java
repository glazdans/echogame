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

    public static Array<Projectile> projectiles = new Array<>();
    public static Array<GameObject> gameObjects = new Array<>();


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

    DebugDrawer debugDrawer;


    GameObject cameraObject;
    PlayerController playerController;
    InputMultiplexer inputMultiplexer;

    Physics physics;


    private PerspectiveCamera camera;

    private float cameraHeight = 18;

    public BulletTestScreen(GdxGame game){
        this.gdxGame = game;
        Bullet.init();
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        physics =  Physics.getInstance();
        physics.setDebugDrawer(debugDrawer);

        camera = new PerspectiveCamera(60,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(new Vector3(0 ,35,-1));
        camera.lookAt(0,0,0);

        cameraObject = GameObjectFactory.getPlayer(physics.collisionWorld,gameObjects);
        playerController = new PlayerController(cameraObject,camera,physics.collisionWorld);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(inputMultiplexer);
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
        playerController.updateMovement();

        for (Projectile projectile : projectiles) {
            projectile.update(delta);
        }
        physics.update();
        cameraObject.update(delta,physics.collisionWorld);

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
        physics.collisionWorld.debugDrawWorld();
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
        physics.dispose();
    }
}
