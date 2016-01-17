package com.glazdans.echo.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.glazdans.echo.component.WeaponComponent;

public class TimeSystem extends IteratingSystem {
    ComponentMapper<WeaponComponent> mWeapon;

    public TimeSystem() {
        super(Aspect.all(WeaponComponent.class));
    }

    @Override
    protected void process(int entityId) {
        WeaponComponent weaponComponent = mWeapon.get(entityId);
        weaponComponent.currentTime += world.getDelta();
    }
}
