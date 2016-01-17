package com.glazdans.echo.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.effects.Effect;

public class WeaponComponent extends Component{
    public Array<Effect> weaponEffects;
    public int weaponId;

    public WeaponComponent(){
        weaponEffects = new Array<>();
    }
}
