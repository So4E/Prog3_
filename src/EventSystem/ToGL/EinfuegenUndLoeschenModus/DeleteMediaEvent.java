package EventSystem.ToGL.EinfuegenUndLoeschenModus;

import Logging.EventType;

import java.util.EventObject;

public class DeleteMediaEvent extends EventObject {
    private String address;
    private EventType eventType = EventType.deletemediaevent;

    public DeleteMediaEvent(Object source, String address) {
        super(source);
        this.address = address;
    }

    public String getAddress() { return address; }
    public EventType getEventType(){return this.eventType;}

    @Override
    public String toString(){ return "DeleteMediaEvent";}
}
