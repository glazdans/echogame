package com.glazdans.echo;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.glazdans.echo.physics.PhysicsEngine;


public class TestScreen implements Screen {
    private GdxGame game;

    DebugRenderer renderer;
    World world;
    Input input;
    Physics physics;
    PhysicsEngine physicsEngine;
    Array<Disposable> disposables = new Array<>();

    Entity player;

    public TestScreen(GdxGame game) {
        this.game = game;

        world = new World();
        renderer = new DebugRenderer();
        physics= new Physics();
        disposables.add(renderer);

        Entity entity = new Entity(10f);
        entity.mousePosition.set(0 ,30);
        entity.position.set(40,40);

        world.addEntity(entity);
        input = new Input(entity.id);
        player=entity;

        physicsEngine = new PhysicsEngine();

        entity = new Entity(10f);
        entity.position.set(100,100);
        entity.velocity.set(-10,0);
        world.addEntity(entity);
        physicsEngine.collisionList.add(entity);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(input);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    @Override
    public void render(float delta) {
        DebugRenderer.camera.update();
        input.updateInput(delta);
        world.update();
        physicsEngine.update(delta,player);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.draw();

    }

    @Override
    public void resize(int width, int height) {
        DebugRenderer.camera.setToOrtho(false,width,height);
        DebugRenderer.camera.position.set(0,0,0);
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
        for(Disposable d : disposables){
            d.dispose();
        }
    }
}
