package com.glazdans.echo.component;

import com.artemis.Component;

public class AttackComponent extends Component {
    public int weaponDamage;
    public int ammoCount;

    public float reloadTime;

    //public int framesForBullet;
    //public int frameTime; TODO when have frame dependant update?

    public float timeForBullet;
    public float currentTime;
}
