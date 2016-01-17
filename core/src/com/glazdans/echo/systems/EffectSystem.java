package com.glazdans.echo.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.component.MovementComponent;
import com.glazdans.echo.component.TransformComponent;
import com.glazdans.echo.component.WeaponComponent;
import com.glazdans.echo.effects.Effect;
import com.glazdans.echo.effects.EffectType;
import com.glazdans.echo.effects.KnockbackEffect;
import com.glazdans.echo.events.*;

public class EffectSystem extends BaseEntitySystem implements EventReceiver{
    ComponentMapper<TransformComponent> mTransform;
    ComponentMapper<MovementComponent> mMovement;
    ComponentMapper<WeaponComponent> mWeapon;

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
            WeaponComponent weaponComponent = mWeapon.get(hitEvent.entityShooterId);
            if(weaponComponent != null){
                for (Effect weaponEffect : weaponComponent.weaponEffects) {
                    applyEffect(weaponEffect,hitEvent);
                }
            }
        }
        hitEvents.clear();
    }

    private void applyEffect(Effect effect, HitEvent event){
        switch (effect.effectType){
            case Knockback:
                applyKnockback(event,(KnockbackEffect) effect);
                break;
            case Damage:
                break;
            default:
                Gdx.app.log("Unrecognized effect",effect.toString());
                break;
        }
    }

    private static Vector3 tmp = new Vector3();
    public void applyKnockback(HitEvent hitEvent, KnockbackEffect effect){
        TransformComponent shot = mTransform.get(hitEvent.entityShotId);
        TransformComponent shooter = mTransform.get(hitEvent.entityShooterId);

        tmp.set(shot.position).sub(shooter.position).nor().scl(effect.knockbackStrength);

        MovementComponent movementComponent = mMovement.get(hitEvent.entityShotId);
        //tmp.set(hitEvent.normal).nor().scl(-knockbackDistance);
        if(movementComponent != null){
            movementComponent.velocity.add(tmp);
        }
        //transformComponent.position.add(tmp);


    }
}
