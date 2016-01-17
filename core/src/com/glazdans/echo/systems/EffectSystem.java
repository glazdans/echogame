package com.glazdans.echo.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.component.MovementComponent;
import com.glazdans.echo.component.TransformComponent;
import com.glazdans.echo.events.*;

public class EffectSystem extends BaseEntitySystem implements EventReceiver{
    ComponentMapper<TransformComponent> mTransform;
    ComponentMapper<MovementComponent> mMovement;

    private Array<HitEvent> hitEvents = new Array<>();

    public EffectSystem(){
        super(Aspect.all());
        EventDispatcher.getInstance().addReceiver(EventType.HitEvent,this);
    }

    @Override
    public void addEvent(Event event) {
        hitEvents.add((HitEvent)event);
    }

    @Override
    protected void processSystem() {
        for (HitEvent hitEvent : hitEvents) {
            applyKnockback(hitEvent,1);
        }
        hitEvents.clear();
    }

    private static Vector3 tmp = new Vector3();
    public void applyKnockback(HitEvent hitEvent, float knockbackDistance){
        TransformComponent shot = mTransform.get(hitEvent.entityShotId);
        TransformComponent shooter = mTransform.get(hitEvent.entityShooterId);

        tmp.set(shot.position).sub(shooter.position).nor().scl(knockbackDistance);

        MovementComponent movementComponent = mMovement.get(hitEvent.entityShotId);
        //tmp.set(hitEvent.normal).nor().scl(-knockbackDistance);
        if(movementComponent != null){
            movementComponent.velocity.add(tmp);
        }
        //transformComponent.position.add(tmp);


    }
}
