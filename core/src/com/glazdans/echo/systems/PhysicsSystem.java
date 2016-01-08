package com.glazdans.echo.systems;

import com.artemis.BaseSystem;
import com.glazdans.echo.bullet.Physics;

public class PhysicsSystem extends BaseSystem {
    public Physics physics;

    @Override
    protected void initialize() {
        physics = Physics.getInstance();
    }

    @Override
    protected void processSystem() {
        physics.update();
    }
}
