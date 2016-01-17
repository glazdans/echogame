package com.glazdans.echo.utils;

import com.glazdans.echo.component.WeaponComponent;

public class WeaponFactory {
    public static void defaultWeapon(WeaponComponent attackComponent){
        attackComponent.ammoCount = 5;
        attackComponent.currentTime = 0;
        attackComponent.weaponDamage = 50;
        attackComponent.timeBetweenShots = 0.5f;
    }
}
