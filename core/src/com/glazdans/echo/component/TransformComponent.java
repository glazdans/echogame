package com.glazdans.echo.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class TransformComponent extends Component {
    public Matrix4 transform;
    public Quaternion rotation;
    public Vector3 position;

    public TransformComponent(){
        transform = new Matrix4();
        rotation = new Quaternion();
        position = new Vector3();
    }
}
