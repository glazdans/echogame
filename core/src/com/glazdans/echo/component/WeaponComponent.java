package com.glazdans.echo.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.effects.Effect;

public class WeaponComponent extends Component{
    public Array<Effect> weaponEffects;
    public int weaponId;

    public int weaponDamage;
    public int ammoCount;

    public float reloadTime;

    //public int framesForBullet;
    //public int frameTime; TODO when have frame dependant update?
    public float timeBetweenShots;
    public float currentTime;

    public WeaponComponent(){
        weaponEffects = new Array<>();
    }
}
