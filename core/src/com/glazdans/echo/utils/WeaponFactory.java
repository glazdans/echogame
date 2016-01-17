package com.glazdans.echo.utils;

import com.glazdans.echo.component.AttackComponent;

public class WeaponFactory {
    public static void defaultWeapon(AttackComponent attackComponent){
        attackComponent.ammoCount = 5;
        attackComponent.currentTime = 0;
        attackComponent.weaponDamage = 50;
    }
}
