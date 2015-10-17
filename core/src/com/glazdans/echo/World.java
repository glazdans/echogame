package com.glazdans.echo;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

public class World {

    public Array<Entity> entities;

    public IntMap<Entity> entitiesById;

    private Array<Command> commandQueue;

    private static int idSequence = 0;

    private static World worldInstance;

    public World(){
        entities = new Array<>();
        entitiesById = new IntMap<>();
        commandQueue = new Array<>();

        worldInstance = this;
    }

    public void addEntity(Entity entity){
        synchronized (entities) {
            entity.id = ++idSequence;
            entities.add(entity);
            entitiesById.put(entity.id, entity);
        }
    }

    public void addCommandToQueue(Command command){
        synchronized (commandQueue) {
            commandQueue.add(command);
        }
    }

    public void update(){
        updateCommandQueue();
        updateWorld();
    }

    private void updateCommandQueue(){
        synchronized (commandQueue){
            for(Command command : commandQueue){
               entitiesById.get(command.entityId).setCommand(command);
                commandQueue.removeValue(command, false);
            }
        }
    }

    private void updateWorld(){
        for(Entity entity : entities){
            entity.update();
        }
    }

    public static World currentInstance(){
        return worldInstance;
    }

}
