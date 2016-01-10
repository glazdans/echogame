package com.glazdans.echo.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.glazdans.echo.bullet.Physics;
import com.glazdans.echo.component.MovementComponent;
import com.glazdans.echo.component.PhysicsComponent;
import com.glazdans.echo.component.PlayerComponent;
import com.glazdans.echo.component.TransformComponent;

public class MovementSystem extends IteratingSystem {
    ComponentMapper<MovementComponent> mMovement;
    ComponentMapper<TransformComponent> mTransform;
    ComponentMapper<PlayerComponent> mPlayer;
    ComponentMapper<PhysicsComponent> mPhysics;

    public MovementSystem() {
        super(Aspect.all(MovementComponent.class, TransformComponent.class));
    }

    private static Vector3 tmp = new Vector3();
    private static Vector2 directionVector = new Vector2();

    @Override
    protected void process(int entityId) {
        float delta = world.getDelta();
        TransformComponent transform = mTransform.get(entityId);
        MovementComponent movement = mMovement.get(entityId);
        PlayerComponent playerComponent = mPlayer.get(entityId);

        if (playerComponent != null) { // TODO Handle collision more cleanly?
            int collisionPositionChangeCount = movement.positionChangeCount;
            Vector3 collisionPositionChanges = movement.positionChange;
            //Gdx.app.log("count:", Integer.toString(collisionPositionChangeCount));
            //Gdx.app.log("amount to change:", collisionPositionChanges.toString());
            if (collisionPositionChangeCount > 0) {
                collisionPositionChanges.scl(1f / collisionPositionChangeCount);
                collisionPositionChanges.scl(-1f); // subtraction
                transform.position.add(collisionPositionChanges.scl(1.3f));
                collisionPositionChanges.setZero();
            }
            movement.positionChangeCount = 0;
            movement.positionChange.set(0, 0, 0);




            //HANDLE DISTANCE TO GROUND
            float distanceFromGround = Physics.getInstance().distanceToGround(transform.position,2f);
            // when the ray went the full length and did not hit the ground, NaN is the return value
            movement.isGrounded = false;
            float embedThreshold = 0f;

            if (!Float.isNaN(distanceFromGround)) {
                if (distanceFromGround < 0.1f) {
                    transform.position.add(tmp.set(0f, -distanceFromGround, 0f));
                    if (movement.velocity.y < 0f) movement.velocity.y = 0f;
                    movement.isGrounded = true;
                }
                if (distanceFromGround < embedThreshold ) {
                    // penetrating into the ground
                    transform.position.y += -distanceFromGround;
                    movement.isGrounded = true;
                } else if (distanceFromGround >= 0f) {
                    Vector3 velocity = movement.velocity;
                    // cap velocity to distance from ground
                    if (velocity.y < 0 && distanceFromGround - velocity.y <= 0f) {
                        velocity.y = -distanceFromGround;
                        System.out.println("cap velocity: " + velocity.y);
                        movement.isGrounded = true;
                    }
                }
            }
            if(!movement.isGrounded){
                movement.acceleration.add(0, -9.81f, 0);
            }else{
                movement.acceleration.y = 0;
            }
        }


        // Handle default update
        Vector3 velocity = movement.velocity;
        Vector3 position = transform.position;

        tmp.set(movement.acceleration);
        tmp.scl(delta);
        velocity.add(tmp);
        // doesn't slow movement on y axis
        tmp.set(movement.velocity);
        velocity = velocity.scl(0.80f);
        velocity.set(velocity.x, tmp.y, velocity.z);
        directionVector.set(velocity.x, velocity.z);

        if (mPlayer.has(entityId)) {
            PlayerComponent player = mPlayer.get(entityId);
            float maxMovementSpeed = player.maxSpeed;
            if (directionVector.len2() > maxMovementSpeed * maxMovementSpeed) {
                directionVector.nor().scl(maxMovementSpeed);
                velocity.set(directionVector.x, velocity.y, directionVector.y);
            }
        }
        tmp.set(velocity);
        tmp.scl(delta);
        position.add(tmp);
        transform.transform.set(position, transform.rotation);

        PhysicsComponent physicsComponent = mPhysics.get(entityId);
        if (physicsComponent != null) {
            physicsComponent.collisionObject.setWorldTransform(transform.transform);
        }
        //TODO IMPLEMENT IN PHYSICS
        //rigidBody.setWorldTransform(transform);
    }
}
