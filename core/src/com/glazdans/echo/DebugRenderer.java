package com.glazdans.echo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

public class DebugRenderer implements Disposable {
    private ShapeRenderer renderer;
    public static OrthographicCamera camera;

    private final float testRadiuss= 5f;
    public DebugRenderer() {
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(0,0,0);
    }


    public void draw(){
        camera.update();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        for(Entity entity : World.currentInstance().entities){
            renderer.setColor(Color.WHITE);
            renderer.circle(entity.position.x,entity.position.y,testRadiuss);

            renderer.setColor(Color.BLUE);
            renderer.line(entity.position.x,entity.position.y,entity.position.x+entity.mousePosition.x,entity.position.y+entity.mousePosition.y);
        }
        renderer.end();
    }
    @Override
    public void dispose() {
        renderer.dispose();
    }
}
