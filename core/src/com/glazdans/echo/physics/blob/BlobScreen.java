package com.glazdans.echo.physics.blob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.physics.Particle;
import com.glazdans.echo.physics.ParticleWorld;

public class BlobScreen implements Screen {
    public static float BLOB_RADIUS =0.5f;
    public static int PLATFORM_COUNT = 10;
    public static int BLOB_COUNT = 5;

    Array<Particle> blobs;
    Array<Platform> platforms;

    ParticleWorld world;
    BlobForceGenerator blobForceGenerator;

    float xAxis;
    float yAxis;
    private static Vector3 tmp = new Vector3();

    //LIBGDX stuff
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;

    public BlobScreen(){
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        xAxis=0;yAxis=0;
        world = new ParticleWorld(PLATFORM_COUNT+BLOB_COUNT,PLATFORM_COUNT);
        blobs = new Array<>(BLOB_COUNT);

        blobForceGenerator = new BlobForceGenerator();
        blobForceGenerator.particles= blobs;
        blobForceGenerator.maxAttraction =20f;
        blobForceGenerator.maxRepulsion = 10f;
        blobForceGenerator.minNaturalDistance = BLOB_RADIUS*0.75f;
        blobForceGenerator.maxNaturalDistance = BLOB_RADIUS*1.5f;
        blobForceGenerator.maxDistance = BLOB_RADIUS * 2.5f;
        blobForceGenerator.maxFloat =2 ;
        blobForceGenerator.floatHead = 8.0f;

        // Create platforms
        platforms = new Array<>(PLATFORM_COUNT);
        for(int i = 0 ; i< PLATFORM_COUNT; i++){
            Platform platform = new Platform();
            platform.start = new Vector3((i%2)*10.0f-5.0f,i*4.0f+((i%2!=0)?0.0f:2.0f),0);
            platform.start.x += MathUtils.random(2f);
            platform.start.y += MathUtils.random(2f);

            platform.end = new Vector3((i%2)*10.0f+5.0f,i*4.0f+((i%2!=0)?0.0f:2.0f),0);
            platform.end.x += MathUtils.random(2f);
            platform.end.y += MathUtils.random(2f);

            platform.particles = blobs;
            platforms.add(platform);
            world.generators.add(platform);
        }

        // Create blobs
        Platform p = platforms.get(PLATFORM_COUNT-2);
        float fraction = 1.0f /BLOB_COUNT;
        Vector3 delta = new Vector3(p.end).sub(p.start);
        for(int i = 0; i<BLOB_COUNT;i++){
            Particle blob = new Particle();
            int me = (i+BLOB_COUNT/2) % BLOB_COUNT;

            tmp.set(delta);
            blob.position.set(p.start).add(tmp.scl(me*0.8f*fraction+-.1f));
            blob.position.add(new Vector3(0,1.0f+MathUtils.random(),0));

            blob.velocity.set(0,0,0);
            blob.damping = 0.2f;
            blob.acceleration.set(new Vector3(Particle.globalGravity).scl(0.4f));
            blob.setMass(1f);
            blob.clearAccumulator();

            blobs.add(blob);
            world.particles.add(blob);
            world.registry.add(blob,blobForceGenerator);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isButtonPressed(Input.Keys.LEFT)){
            xAxis -=1;
        }
        if(Gdx.input.isButtonPressed(Input.Keys.RIGHT)){
            xAxis +=1;
        }
        if(Gdx.input.isButtonPressed(Input.Keys.DOWN)){
            yAxis -=1;
        }
        if(Gdx.input.isButtonPressed(Input.Keys.UP)){
            yAxis +=1;
        }
        world.startFrame();

        blobs.get(0).addForce(new Vector3(xAxis,yAxis,0).scl(10f));

        world.runPhysics(delta);

        for(Particle b : blobs){
            b.position.z =0;
        }

        camera.position.set(blobs.get(0).position);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(Platform platform : platforms){
            shapeRenderer.line(platform.start,platform.end);
        }
        for(Particle particle : blobs){
            shapeRenderer.circle(particle.position.x,particle.position.y,BLOB_RADIUS);
        }
        shapeRenderer.end();

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false,width/5,height/5);
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
        shapeRenderer.dispose();
    }
}
