package com.glazdans.echo.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.glazdans.echo.component.MovementComponent;

public class Physics implements Disposable {
    private static Physics instance;
    public Array<Disposable> disposables;

    private static Vector3 tmp = new Vector3();
    private static Vector3 tmp2 = new Vector3();
    private static Vector3 tmp3 = new Vector3();

    public final static short GROUND_FLAG = 1<<8;
    public final static short OBJECT_FLAG = 1<<9;
    public final static short ALL_FLAG = -1;

    private MyContactListener contactListener;

    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private btBroadphaseInterface broadphase;

    public btCollisionWorld collisionWorld;

    private Physics(){
        init();
    }

    public static Physics getInstance(){
        if(instance == null){
            instance = new Physics();
        }
        return instance;
    }
    private void init(){
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher,broadphase,collisionConfig);
        contactListener = new MyContactListener();
        disposables = new Array<>();
    }
    public static void addStaticObject(btCollisionObject object){
        getInstance().collisionWorld.addCollisionObject(object,GROUND_FLAG,OBJECT_FLAG);
    }

    public static void addDynamicObject(btCollisionObject object){
        getInstance().collisionWorld.addCollisionObject(object,OBJECT_FLAG,ALL_FLAG);
    }

    public void setDebugDrawer(DebugDrawer debugDrawer){
        collisionWorld.setDebugDrawer(debugDrawer);
    }

  /*  public float distanceFromGroundFast(Vector3 position, Vector3 dimen) { // TODO DELETE WHEN SAFE
        float hitDist = Float.NaN;
        float downStep = 2f + dimen.y; // length of ray
        float rayOriginHeight = 0f;
        // test single point
        rectVectors[0].set(position);
        Vector3 point = rectVectors[0];
        castRayStaticOnly(point, tmp.set(point).sub(0f, downStep, 0f));
        if (raycastReport.hit) {
            hitDist = raycastReport.hitDistance;
        }
        if (!Float.isNaN(hitDist)) {
            // if embedded in ground, a negative value will be returned
            hitDist -= (rayOriginHeight + dimen.y/2f);
        }
        lastDistanceFromGroundAvgDist = hitDist;
        return hitDist;
    }*/

    ClosestRayResultCallback groundDistanceCallback = new ClosestRayResultCallback(new Vector3(),new Vector3());

    public float distanceToGround(Vector3 position, float halfDistance){
        groundDistanceCallback.setRayFromWorld(position);
        Vector3 directionVector = new Vector3(0,-1,0);
        directionVector.scl(10).add(position);
        groundDistanceCallback.setRayToWorld(directionVector);
        groundDistanceCallback.setClosestHitFraction(1f);
        groundDistanceCallback.setCollisionObject(null);
        groundDistanceCallback.setCollisionFilterGroup(OBJECT_FLAG);
        groundDistanceCallback.setCollisionFilterMask(GROUND_FLAG);
        collisionWorld.rayTest(position,directionVector,groundDistanceCallback);

        if(groundDistanceCallback.hasHit()){
            float length = position.dst(directionVector) *  groundDistanceCallback.getClosestHitFraction();
            return length - halfDistance;
        }
        return Float.NaN;
    }

    class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded(btManifoldPoint cp, btCollisionObject colObj0, int partId0, int index0, btCollisionObject colObj1, int partId1, int index1) {
            Integer a = (Integer) colObj0.userData;
            Integer b = (Integer) colObj1.userData;

            if (a != null || b != null) {
                // the distance between the point on body A that collided and the point on body B that collided
                float dist = cp.getDistance();
                // the normal vector of the collision
                cp.getNormalWorldOnB(norm);
                cp.getPositionWorldOnA(worldA);
                cp.getPositionWorldOnA(worldB);
                if (a != null) {
                    entityCollision(a, norm, worldA, worldB, dist, b);
                }
                // for object b, we must reverse the normal direction
                if (b != null) {
                    entityCollision(b, norm.scl(-1f), worldB, worldA, dist, a);
                }
            }
            return true;
        }
        private GameObject getEntity(btCollisionObject obj) {
            if (obj.userData instanceof GameObject) return (GameObject) obj.userData;
            if(obj.userData instanceof Integer){

            }
            return null;
        }

        private Vector3 norm = new Vector3();
        private Vector3 worldA = new Vector3();
        private Vector3 worldB = new Vector3();

        /** this is where the magic happens! */
        private void entityCollision(Integer entityId, Vector3 normal, Vector3 myPoint, Vector3 otherPoint, float dist, Integer otherEntityId) {
            if (Math.abs(dist) < 0.01f) return; // ignoring tiny collisions seems to help stability?
            MovementComponent movementComponent = BulletTestScreen.world.getMapper(MovementComponent.class).get(entityId);
            if(movementComponent == null){
                return;
            }

            // Change Position
            tmp.set(myPoint).sub(otherPoint).nor().add(normal); // direction of vel + normal
            tmp.nor();
            Vector3 posDelta = tmp3.set(tmp).scl(dist);

            movementComponent.positionChange.add(posDelta);
            movementComponent.positionChangeCount++;
            //ent.addCollisionPositionChange(posDelta);

            // Change Velocity
            // slide along the faces of geometry, but bounce off other entities
            Vector3 vel = movementComponent.velocity;
            boolean bounce = otherEntityId != null;
            if (!bounce) {
                // slide along the wall
                // TODO don't slide "up" vertical inclines of a certain steepness
                projectVectorOntoPlane(vel, normal);
            } else {
                // bounce off of other entities
				/*float speed = vel.len();
				tmp4.set(vel).nor(); // entity unit velocity
				float dotScalar = tmp2.set(tmp4).dot(norm);
				tmp3.set(norm).scl(-2f * dotScalar);
				tmp2.set(tmp4).add(tmp3).nor();
				tmp2.scl(speed);
				// don't bounce up or down
				tmp2.y = 0f;
				ent.adjustVelocity(tmp2);
				otherEntity.adjustVelocity(tmp2.scl(-1f));*/
            }
        }

    }

    private static void projectVectorOntoPlane(Vector3 vec, Vector3 planeNorm) {
        // Formula: vector v1_projected = v1 - Dot(v1, n) * n;
        tmp2.set(planeNorm).scl(tmp.set(vec).dot(planeNorm));
        vec.sub(tmp2);
    }

    public void update(){
        collisionWorld.performDiscreteCollisionDetection();
    }

    @Override
    public void dispose() {
        broadphase.dispose();
        collisionConfig.dispose();
        dispatcher.dispose();
        collisionWorld.dispose();
        contactListener.dispose();
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }
}
