package com.glazdans.echo.physics.firework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.glazdans.echo.physics.firework.Firework;
import com.glazdans.echo.physics.firework.FireworkRule;

public class FireworksDemoScreen implements Screen {

    Pool<Firework> fireworkPool;
    Array<Firework> fireworks;

    SpriteBatch spriteBatch;

    FireworkRule[] rules = new FireworkRule[2];


    Texture yellowParticle;
    Texture redParticle;

    OrthographicCamera camera;

    public FireworksDemoScreen() {
        spriteBatch = new SpriteBatch();
        fireworkPool = new Pool<Firework>() {
            @Override
            protected Firework newObject() {
                return new Firework();
            }
        };
        fireworks = new Array<>();
        initRules();

        yellowParticle = new Texture(Gdx.files.internal("particles\\ParticleYellow.png"));
        redParticle = new Texture(Gdx.files.internal("particles\\ParticleRed.png"));

        fireworks.add(rules[1].create(fireworkPool.obtain(), null));
        camera = new OrthographicCamera();

    }

    public void createNewFirework(){
        Firework firework = fireworkPool.obtain();
        fireworks.add(rules[1].create(firework, null));
        firework.position.set(MathUtils.random(-300,300),0,0);
    }

    private void initRules() {
        // Rule 1
        FireworkRule rule = new FireworkRule();
        rule.damping = 0.5f;
        rule.maxAge = 2f;
        rule.minAge = 1.0f;
        rule.maxVelocity = new Vector3(150, 200, 150);
        rule.minVelocity = new Vector3(-225, -105, -225);
        rule.type = 1;
        rule.payloadCount = 0;
        rule.payload = null;
        rules[0] = rule;

        // Rule 2
        rule = new FireworkRule();
        rule.damping = 0.34f;
        rule.maxAge = 0.8f;
        rule.minAge = 0.5f;
        rule.maxVelocity = new Vector3(2, 500, 2);
        rule.minVelocity = new Vector3(1, 270, 1);
        rule.type = 2;
        rule.payloadCount = 10;
        FireworkRule.Payload payload = new FireworkRule.Payload();
        payload.type = 1;
        payload.count = 50;
        rule.payload = payload;
        rules[1] = rule;
    }

    private void handleParticleDeath(Firework firework){
        FireworkRule rule = rules[firework.type-1];
        if(rule.payload != null){
            int count = rule.payload.count;
            rule = rules[rule.payload.type -1];
            for(int i =0 ; i<count;i++){
                fireworks.add(rule.create(fireworkPool.obtain(), firework));
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        for (Firework firework : fireworks) {
            firework.update(delta);
            if (firework.age < 0) { // Return to pool when firework is dead and create offspring
                handleParticleDeath(firework);
                fireworks.removeValue(firework, false);
                fireworkPool.free(firework);
            }
        }
        if(Gdx.input.justTouched()){
            createNewFirework();
        }

        camera.update();
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.enableBlending();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for(Firework firework : fireworks){
            Color c = spriteBatch.getColor();
            spriteBatch.setColor(c.r,c.g,c.b,1);
            if(firework.type ==1){
                if(firework.age <=0.125f){
                    spriteBatch.setColor(c.r,c.g,c.b,firework.age/0.125f);
                }
                spriteBatch.draw(redParticle,firework.position.x,firework.position.y);
            }
            if(firework.type ==2){
                spriteBatch.draw(yellowParticle,firework.position.x,firework.position.y);
            }
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false,width,height);
        camera.position.set(-10,height/2,0);
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
        spriteBatch.dispose();
    }
}
