package com.glazdans.echo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
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

    private static Vector3 tmp = new Vector3();
    public void draw(){
        camera.update();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        for(Terrain t : Physics.getCurrentInstance().levelObjects){
            renderer.setColor(Color.RED);
            renderer.rect(t.position.x,t.position.y,Physics.TILE_SIZE,Physics.TILE_SIZE);
        }
        for(Entity entity : World.currentInstance().entities){
            renderer.setColor(Color.WHITE);
            renderer.rect(entity.position.x, entity.position.y, Physics.TILE_SIZE, Physics.TILE_SIZE);
            renderer.setColor(Color.WHITE);
            renderer.line(entity.position.x+8,entity.position.y+8,entity.position.x+entity.mousePosition.x,entity.position.y+entity.mousePosition.y);
            renderer.setColor(Color.RED);
            renderer.line(entity.position.x,entity.position.y,entity.position.x+entity.mousePosition.x,entity.position.y+entity.mousePosition.y);
        }
        tmp = camera.unproject(tmp.set(Gdx.input.getX(), Gdx.input.getY(),0));
        renderer.circle(tmp.x,tmp.y, 10);
        renderer.end();
    }
    @Override
    public void dispose() {
        renderer.dispose();
    }
}
