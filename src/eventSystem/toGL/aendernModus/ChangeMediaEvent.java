package eventSystem.toGL.aendernModus;

import eventSystem.EventType;

import java.util.EventObject;

public class ChangeMediaEvent extends EventObject {
    private String address;
    private EventType eventType = EventType.changemediaevent;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ChangeMediaEvent(Object source, String address) {
        super(source);
        this.address = address;
    }

    public String getAddress() { return this.address; }

    public EventType getEventType(){return this.eventType;}

    @Override
    public String toString(){ return "ChangeMediaEvent";}
}