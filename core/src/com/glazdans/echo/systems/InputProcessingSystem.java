package com.glazdans.echo.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector3;
import com.glazdans.echo.component.InputComponent;
import com.glazdans.echo.component.MovementComponent;
import com.glazdans.echo.component.PlayerComponent;

public class InputProcessingSystem extends BaseEntitySystem {
    ComponentMapper<InputComponent> mInput;
    ComponentMapper<MovementComponent> mMovement;
    ComponentMapper<PlayerComponent> mPlayer;



    public InputProcessingSystem(){
        super(Aspect.all(InputComponent.class,MovementComponent.class,PlayerComponent.class));
    }

    @Override
    protected void processSystem() {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); s > i; i++) {
            process(ids[i]);
        }
    }
    private static Vector3 tmp = new Vector3();
    private void process(int id){
        InputComponent input = mInput.get(id);
        float movementSpeed = mPlayer.get(id).speedGain;
        tmp.set(0,0,0);
        if(input.pressUp){
            tmp.add(0,0,1);
        }
        if(input.pressDown){
            tmp.add(0,0,-1);
        }
        if(input.pressLeft){
            tmp.add(1,0,0);
        }
        if(input.pressRight){
            tmp.add(-1,0,0);
        }

        tmp.nor().scl(movementSpeed);
        MovementComponent movement = mMovement.get(id);
        tmp.y = movement.acceleration.y;
        movement.acceleration.set(tmp);

      /*  gameObject.updateAcceleration(tmp); TODO DELETE OLD CODE

        direction.nor();
        acceleration.set(direction.scl(movementSpeed));*/
    }
}
