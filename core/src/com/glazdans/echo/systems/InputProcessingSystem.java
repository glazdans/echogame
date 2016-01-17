package com.glazdans.echo.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.component.MovementComponent;
import com.glazdans.echo.component.PlayerComponent;
import com.glazdans.echo.component.TransformComponent;
import com.glazdans.echo.events.*;

public class InputProcessingSystem extends BaseEntitySystem implements EventReceiver {
    ComponentMapper<MovementComponent> mMovement;
    ComponentMapper<PlayerComponent> mPlayer;
    ComponentMapper<TransformComponent> mTransform;

    private final Array<Event> eventQueue;

    AttackSystem attackSystem;
    @Override
    public void addEvent(Event event) {
        synchronized (eventQueue){
            eventQueue.add(event);
        }
    }

    public InputProcessingSystem(){
        super(Aspect.all(MovementComponent.class,PlayerComponent.class));
        eventQueue = new Array<>();
        EventDispatcher.getInstance().addReceiver(EventType.ActionEvent, this);
    }

    @Override
    protected void processSystem() {
        synchronized (eventQueue){
            for(Event event : eventQueue){
                ActionEvent actionEvent = (ActionEvent)event;
                process(actionEvent);
            }
            eventQueue.clear();
        }
    }
    private static Vector3 tmp = new Vector3();

    private void process(ActionEvent event){
        int id = event.id;
        float movementSpeed = mPlayer.get(id).speedGain;
        tmp.set(event.direction);

        tmp = tmp.nor().scl(movementSpeed);
        MovementComponent movement = mMovement.get(id);
        tmp.y = movement.acceleration.y;
        movement.acceleration.set(tmp);

        TransformComponent transformComponent = mTransform.get(id);
        transformComponent.rotation.setEulerAngles(event.degree,0,0);

        if(event.shoot){
            attackSystem.addShoot(id);
        }

      /*  gameObject.updateAcceleration(tmp); TODO DELETE OLD CODE

        direction.nor();
        acceleration.set(direction.scl(movementSpeed));*/
    }
}
