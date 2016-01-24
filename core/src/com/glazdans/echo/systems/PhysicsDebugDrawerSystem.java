package com.glazdans.echo.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.glazdans.echo.bullet.BulletTestScreen;

public class PhysicsDebugDrawerSystem extends BaseSystem {
    private DebugDrawer debugDrawer;

    private PhysicsSystem physicsSystem;

    public static Vector3 from = new Vector3();
    public static Vector3 to = new Vector3();

    public static Matrix4 startingPoint = new Matrix4();

    public boolean debug = true;
    @Override
    protected void initialize() {
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

        physicsSystem.physics.collisionWorld.setDebugDrawer(debugDrawer);
    }

    private static Vector3 red = new Vector3(1,0,0);

    @Override
    protected void processSystem() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.X)){
            debug = !debug;
        }
        if(debug) {
            debugDrawer.begin(BulletTestScreen.camera);
            physicsSystem.physics.collisionWorld.debugDrawWorld();
            physicsSystem.physics.collisionWorld.getDebugDrawer().drawLine(from, to, red);
            physicsSystem.physics.collisionWorld.getDebugDrawer().drawSphere(0.1f, startingPoint, red);
            debugDrawer.end();
        }
    }
}
