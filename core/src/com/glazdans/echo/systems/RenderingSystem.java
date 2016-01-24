package com.glazdans.echo.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.glazdans.echo.bullet.BulletTestScreen;
import com.glazdans.echo.bullet.Physics;
import com.glazdans.echo.component.RenderingComponent;
import com.glazdans.echo.component.TransformComponent;

public class RenderingSystem extends IteratingSystem{
    ComponentMapper<TransformComponent> mTransform;
    ComponentMapper<RenderingComponent> mRendering;

    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;
    private ModelInstance modelInstance;

    private static Matrix4 tmp = new Matrix4();
    public RenderingSystem(){
        super(Aspect.all(RenderingComponent.class, TransformComponent.class));
        camera = BulletTestScreen.camera;
        modelInstance = new ModelInstance(BulletTestScreen.modelLoader.loadSoldier());

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, .7f));
        //environment.set(new ColorAttribute(ColorAttribute.Diffuse, 0.8f, 0.8f, 0.8f, 1.0f));
        DirectionalLight light = new DirectionalLight();
        light.set( 0.4f, 0.4f, 0.4f,new Vector3(1f,-.75f,0).nor());
        environment.add(light);
    }

    @Override
    protected void begin() {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(camera);
    }

    @Override
    protected void end() {
        modelBatch.end();
    }

    @Override
    protected void process(int entityId) {
        TransformComponent transform = mTransform.get(entityId);
        tmp.set(transform.transform);
        modelInstance.transform = tmp;
        PhysicsDebugDrawerSystem.startingPoint.set(transform.transform);
        modelInstance.transform.rotate(0,1,0,180);
        modelInstance.transform.translate(0,-1.5f,0);
        modelBatch.render(modelInstance,environment);
        //modelBatch.render(modelInstance);


    }
}
