package com.glazdans.echo.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.glazdans.echo.components.MovementComponent;
import com.glazdans.echo.components.PlayerComponent;
import com.glazdans.echo.components.TransformComponent;

public class MovementSystem extends IteratingSystem {
    ComponentMapper<MovementComponent> mMovement;
    ComponentMapper<TransformComponent> mTransform;
    ComponentMapper<PlayerComponent> mPlayer;

    public MovementSystem(){
        super(Aspect.all(MovementComponent.class, TransformComponent.class));
    }

    private static Vector3 tmp = new Vector3();
    private static Vector2 directionVector = new Vector2();
    @Override
    protected void process(int entityId) {
        float delta = world.getDelta();
        TransformComponent transform = mTransform.get(entityId);
        MovementComponent movement = mMovement.get(entityId);

        Vector3 velocity = movement.velocity;
        Vector3 position = transform.position;

        tmp.set(movement.acceleration);
        tmp.scl(delta);
        velocity.add(tmp);
        // doesn't slow movement on y axis
        tmp.set(movement.velocity);
        velocity =  velocity.scl(0.80f);
        velocity.set(velocity.x,tmp.y,velocity.z);
        directionVector.set(velocity.x,velocity.z);

        if(mPlayer.has(entityId)){
            PlayerComponent player = mPlayer.get(entityId);
            float maxMovementSpeed = player.maxSpeed;
            if(directionVector.len2() > maxMovementSpeed*maxMovementSpeed){
                directionVector.nor().scl(maxMovementSpeed);
                velocity.set(directionVector.x,velocity.y,directionVector.y);
            }
        }
        tmp.set(velocity);
        tmp.scl(delta);
        position.add(tmp);
        transform.transform.set(position, transform.rotation);
        //TODO IMPLEMENT IN PHYSICS
        //rigidBody.setWorldTransform(transform);
    }
}
