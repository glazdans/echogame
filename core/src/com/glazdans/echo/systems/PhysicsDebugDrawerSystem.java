package com.glazdans.echo.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.glazdans.echo.bullet.BulletTestScreen;

public class PhysicsDebugDrawerSystem extends BaseSystem {
    private DebugDrawer debugDrawer;

    private PhysicsSystem physicsSystem;

    public static Vector3 from = new Vector3();
    public static Vector3 to = new Vector3();

    @Override
    protected void initialize() {
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

        physicsSystem.physics.collisionWorld.setDebugDrawer(debugDrawer);
    }

    @Override
    protected void begin() {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    private static Vector3 red = new Vector3(1,0,0);

    @Override
    protected void processSystem() {
        debugDrawer.begin(BulletTestScreen.camera);
        physicsSystem.physics.collisionWorld.debugDrawWorld();
        physicsSystem.physics.collisionWorld.getDebugDrawer().drawLine(from,to,red);
        debugDrawer.end();

    }
}
