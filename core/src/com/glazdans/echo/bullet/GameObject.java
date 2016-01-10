package com.glazdans.echo.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;

public class GameObject {
    public btCollisionObject rigidBody;
    public Matrix4 transform;

    public Quaternion rotation;
    public Vector3 position;

    public Vector3 acceleration;
    public Vector3 velocity;

    private float movementSpeed = 350;
    private float maxMovementSpeed= 10f;
    public boolean isGrounded;
    float distanceFromGround;

    final boolean isDynamic;

    private Vector3 collisionPositionChanges;
    int collisionPositionChangeCount;


    public GameObject(btCollisionShape collisionShape, boolean isDynamic) {
        rigidBody = new btCollisionObject();
        rigidBody.setCollisionShape(collisionShape);
        transform = new Matrix4(rigidBody.getWorldTransform());
        acceleration = new Vector3();
        velocity = new Vector3();
        collisionPositionChanges = new Vector3();
        this.isDynamic = isDynamic;

        position = new Vector3();
        rotation = new Quaternion();
        position.y = 10;
    }

    public void addCollisionPositionChange(Vector3 posDelta) {
        collisionPositionChangeCount++;
        collisionPositionChanges.add(posDelta);
    }

    public void updateAcceleration(Vector3 direction){
        direction.nor();
        acceleration.set(direction.scl(movementSpeed));
    }
    public void setRotation(float angleY){
        rotation.setEulerAngles(angleY,0,0);
    }
    private static Vector3 tmp = new Vector3();
    private static Vector2 directionVector = new Vector2();
    public void update(float delta,btCollisionWorld world){
        if(!isDynamic){
            return;
        }
        //world.updateAabbs();
        if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)){
            position.y += 1f;
        }
        handleCollisions();

        distanceFromGround = Physics.getInstance().distanceToGround(position,2f);
        // when the ray went the full length and did not hit the ground, NaN is the return value
        isGrounded = false;
        float embedThreshold = 0f;

        if (!Float.isNaN(distanceFromGround)) {
            if (distanceFromGround < 0.1f) {
                adjustPosition(tmp.set(0f, -distanceFromGround, 0f));
                if (velocity.y < 0f) velocity.y = 0f;
                isGrounded = true;
            }
            if (distanceFromGround < embedThreshold ) {
                // penetrating into the ground
                position.y += -distanceFromGround;
                isGrounded = true;
            } else if (distanceFromGround >= 0f) {
                Vector3 velocity = this.velocity;
                // cap velocity to distance from ground
                if (velocity.y < 0 && distanceFromGround - velocity.y <= 0f) {
                    velocity.y = -distanceFromGround;
                    System.out.println("cap velocity: " + velocity.y);
                    isGrounded = true;
                }
            }
        }
        if(!isGrounded){
            acceleration.add(0, -9.81f, 0);
        }else{
            acceleration.y = 0;
        }

     /*   btCollisionObject  collisionObject = rayTest(world);
        if(collisionObject != null) {
            isGrounded = true;
        } else {
            isGrounded = false;
        }

        if(!isGrounded){
            acceleration.add(0, -9.81f, 0);
        }else{
            acceleration.y = 0;
        }
        Gdx.app.log("IsGrounded",Boolean.toString(isGrounded));*/
        /*Gdx.app.log("IsGrounded",Boolean.toString(isGrounded));
        Gdx.app.log("DistanceFromGround",Float.toString(distanceFromGround));*/

        tmp.set(acceleration); //TODO IN MOVEMENT SYSTEM - delete this
        tmp.scl(delta);
        velocity.add(tmp);
        tmp.set(velocity);
        velocity = velocity.scl(0.80f);
        velocity.set(velocity.x,tmp.y,velocity.z);
        directionVector.set(velocity.x,velocity.z);
        if(directionVector.len2() > maxMovementSpeed*maxMovementSpeed){
            directionVector.nor().scl(maxMovementSpeed);
            velocity.set(directionVector.x,velocity.y,directionVector.y);
        }
        tmp.set(velocity);
        tmp.scl(delta);
        position.add(tmp);
        transform.set(position,rotation);
        rigidBody.setWorldTransform(transform);
    }

    public void shoot(){
        Vector3 direction = new Vector3(0,0,1);
        direction = direction.mul(rotation);
        Vector3 startingPosition = new Vector3(direction);
        startingPosition.scl(1f).add(position);
        Projectile projectile = new Projectile(startingPosition,direction,21f,Physics.getInstance().collisionWorld);
        //BulletTestScreen.projectiles.add(projectile);
    }

    public void handleCollisions() {
        if (collisionPositionChangeCount > 0) {
            collisionPositionChanges.scl(1f / collisionPositionChangeCount);
            collisionPositionChanges.scl(-1f); // subtraction
            adjustPosition(collisionPositionChanges.scl(1.3f));
            collisionPositionChangeCount = 0;
            collisionPositionChanges.setZero();
        }
    }

    private void adjustPosition(Vector3 change){
        position.add(change);
    }

    private static final Vector3 rayFrom = new Vector3();
    private static final Vector3 rayTo = new Vector3();
    private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);

    public btCollisionObject rayTest(btCollisionWorld collisionWorld) {
        rayFrom.set(rigidBody.getWorldTransform().getTranslation(new Vector3()));
        // 50 meters max from the origin
        rayTo.set(rigidBody.getWorldTransform().getTranslation(new Vector3()).add(new Vector3(0, -3/2f-0.5f, 0))); // -3/2f-0.5f

        // we reuse the ClosestRayResultCallback, thus we need to reset its
        // values
        callback.setCollisionObject(null);
        callback.setClosestHitFraction(10);
        callback.setRayFromWorld(rayFrom);
        callback.setRayToWorld(rayTo);

        collisionWorld.rayTest(rayFrom, rayTo, callback);

        if (callback.hasHit()) {
            //Gdx.app.log("GameObject rayTest ","callback");
            return callback.getCollisionObject();
        }

        return null;
    }
}
