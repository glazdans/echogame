package com.glazdans.echo.bullet;


import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.model.Node;
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
import com.glazdans.echo.GdxGame;
import com.glazdans.echo.bullet.input.PlayerController;
import com.glazdans.echo.systems.*;
import com.glazdans.echo.utils.EntityFactory;

import java.util.ArrayList;

public class BulletTestScreen implements Screen {
    final GdxGame gdxGame;

    PlayerController playerController;
    CameraInputController cameraInputController;
    InputMultiplexer inputMultiplexer;


    public static PerspectiveCamera camera;

    ModelLoader modelLoader;
    public static World world;


    int cameraEntity;

    InputSystem inputSystem;

    public BulletTestScreen(GdxGame game){
        this.gdxGame = game;
        Bullet.init();
        Physics.getInstance();

        inputSystem = new InputSystem();

        WorldConfiguration configuration = new WorldConfigurationBuilder()
                .with(new InputProcessingSystem(),
                        new MovementSystem(),
                        new PhysicsSystem(),
                        new PhysicsDebugDrawerSystem(),
                        inputSystem,
                        new AttackSystem(),
                        new EffectSystem())
                .build();
        world = new World(configuration);

        cameraEntity = EntityFactory.playerEntity(world);
        inputSystem.setPlayer1Id(cameraEntity);


        camera = new PerspectiveCamera(60,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(new Vector3(0 ,35,-5));
        camera.lookAt(0,0,0);

        //GameObjectFactory.createObjects(BulletTestScreen.gameObjects);

        modelLoader = new ModelLoader();
        Array<Node> modelNodes = modelLoader.loadModel().nodes;
        for (Node modelNode : modelNodes) {
            btCollisionShape shape = Bullet.obtainStaticNodeShape(modelNode, true);
            int entityId = EntityFactory.createStaticObject(shape,world);
        }

        //cameraObject = GameObjectFactory.getPlayer(gameObjects);
        //playerController = new PlayerController(cameraObject,camera,Physics.getInstance().collisionWorld);
        inputMultiplexer = new InputMultiplexer();
        cameraInputController = new CameraInputController(camera);
        //inputMultiplexer.addProcessor(playerController);
        inputMultiplexer.addProcessor(inputSystem);
        inputMultiplexer.addProcessor(cameraInputController);
        Gdx.input.setInputProcessor(inputMultiplexer);

        EntityFactory.createEnemyObject(world,new Vector3(10,10,0));
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
        world.setDelta(delta);
        world.process();

        //playerController.updateMovement();

      /*  for (Projectile projectile : projectiles) {
            projectile.update(delta);
        }
        */
        //cameraObject.update(delta,Physics.getInstance().collisionWorld);

        //camera.position.set(cameraObjectVector.x,cameraHeight,cameraObjectVector.z-3);
        //camera.lookAt(cameraObject.position);
        camera.update();




        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            new Boolean("fak this hsit");
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.PLUS)){

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.MINUS)){

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            gdxGame.setScreen(new BulletTestScreen(gdxGame));
        }


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
        Physics.getInstance().dispose();
        world.dispose();
    }
}
