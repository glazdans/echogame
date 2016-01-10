package com.glazdans.echo.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class MovementComponent extends Component {
    public Vector3 velocity;
    public Vector3 acceleration;

    //TODO IN DIFFERENT COMPONENTS? Unity script like?
    public Vector3 positionChange;
    public int positionChangeCount;

    public boolean isGrounded;

    public MovementComponent(){
        velocity = new Vector3();
        acceleration = new Vector3();
        positionChange = new Vector3();
    }
}
