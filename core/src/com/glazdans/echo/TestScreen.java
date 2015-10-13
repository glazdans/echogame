package com.glazdans.echo;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;


public class TestScreen implements Screen {
    private GdxGame game;

    private ShapeRenderer shapeRenderer;
    //This is a comment s
    //This is a edit for branch
    // Test commit HeLL GOTI HUB

    Array<Disposable> disposables = new Array<Disposable>();

    public TestScreen(GdxGame game) {
        this.game = game;

        disposables.add(shapeRenderer);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
        for(Disposable d : disposables){
            d.dispose();
        }
    }
}
