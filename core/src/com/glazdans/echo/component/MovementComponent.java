package com.glazdans.echo.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class MovementComponent extends Component {
    public Vector3 velocity;
    public Vector3 acceleration;

    public MovementComponent(){
        velocity = new Vector3();
        acceleration = new Vector3();
    }
}
