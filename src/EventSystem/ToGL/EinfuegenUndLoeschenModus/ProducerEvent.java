package EventSystem.ToGL.EinfuegenUndLoeschenModus;

import Logging.EventType;

import java.util.EventObject;

public class ProducerEvent extends EventObject{
    private String name;
    boolean addOrDelete; //add=true, delete=false
    private EventType eventType = EventType.producerevent;

    public ProducerEvent(Object source, String name, boolean addOrDelete) {
        super(source);
        this.name = name;
        this.addOrDelete = addOrDelete;
    }

    public String getName() { return name; }
    public boolean getAddOrDelete() {return addOrDelete;}
    public EventType getEventType(){return this.eventType;}

    @Override
    public String toString(){ return "ProducerEvent";}
}
