package com.glazdans.echo.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.bullet.Physics;
import com.glazdans.echo.component.TransformComponent;
import com.glazdans.echo.events.EventDispatcher;
import com.glazdans.echo.events.HitEvent;

public class AttackSystem extends BaseEntitySystem {
    ComponentMapper<TransformComponent> mTransform;

    Array<Integer> entitiesWhichAreAttacking;

    public void addShoot(int entityId){
        entitiesWhichAreAttacking.add(entityId);
    }

    public AttackSystem(){
        super(Aspect.all());
        entitiesWhichAreAttacking = new Array<>();

    }
    private static Vector3 tmp = new Vector3();
    private static Vector3 tmp1 = new Vector3();
    @Override
    protected void processSystem() {
        for (Integer entityId : entitiesWhichAreAttacking) {
            TransformComponent transform = mTransform.get(entityId);
            Vector3 fak = new Vector3(0,0,1);
            fak.mul(transform.rotation).nor();
            tmp1.set(fak);

            tmp.set(transform.position);
            tmp.add(tmp1.scl(0.65f));

            fak.scl(30);// TODO MAKE BETTER NAMES FOR VARIABLES
            Vector3 direction = fak.add(transform.position);
            shoot(tmp, direction);

            if(callback.hasHit()){
                HitEvent hitEvent = new HitEvent();
                callback.getHitNormalWorld(hitEvent.normal);
                callback.getHitPointWorld(hitEvent.hitPoint);
                hitEvent.entityShooterId = entityId;
                hitEvent.entityShotId = (int) callback.getCollisionObject().userData;
                EventDispatcher.getInstance().addEvent(hitEvent);

                // Debug stuff - refactor to a better system;
                Vector3 uh = new Vector3();
                callback.getHitPointWorld(uh);
                //Gdx.app.log("Has hit at:",uh.toString());
                PhysicsDebugDrawerSystem.from.set(tmp);
                PhysicsDebugDrawerSystem.to.set(uh);

            }
        }
        entitiesWhichAreAttacking.clear();
    }

    private static ClosestRayResultCallback callback = new ClosestRayResultCallback(new Vector3(), new Vector3());
    private void shoot(Vector3 start, Vector3 direction){
        callback.setRayFromWorld(start);
        callback.setRayToWorld(direction);

        callback.setCollisionFilterGroup(Physics.OBJECT_FLAG);
        callback.setCollisionFilterMask(Physics.ALL_FLAG);
        callback.setCollisionObject(null);
        callback.setClosestHitFraction(1);
        Physics.getInstance().collisionWorld.rayTest(start,direction,callback);

    }
}
