package com.glazdans.echo.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventDispatcher {

    private Map<EventType,Array<EventReceiver>> receiverMap;
    private static EventDispatcher instance;

    public static EventDispatcher getInstance(){
        if(instance == null){
            instance = new EventDispatcher();
        }
        return instance;
    }

    public EventDispatcher(){
        receiverMap = new HashMap<>();

    }

    public void addReceiver(EventType eventType, EventReceiver receiver){
        Array<EventReceiver> eventReceivers = receiverMap.get(eventType);
        if(eventReceivers == null){
            eventReceivers = new Array<>();
            receiverMap.put(eventType,eventReceivers);
        }
        eventReceivers.add(receiver);
    }

    public void removeReceiver(EventReceiver receiver){
        Collection<Array<EventReceiver>> collection = receiverMap.values();
        for (Array<EventReceiver> eventReceivers : collection) {
            eventReceivers.removeValue(receiver,false);
        }
    }

    public void addEvent(Event event){
        Array<EventReceiver> eventReceivers = receiverMap.get(event.eventType);
        if(eventReceivers == null){
            Gdx.app.log("EventDispatcher", "No receivers for event:" + event.eventType.toString());
        }

        for (EventReceiver eventReceiver : eventReceivers) {
            eventReceiver.addEvent(event);
        }

    }

}
