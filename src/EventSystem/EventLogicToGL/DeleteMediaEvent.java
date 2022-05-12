package EventSystem.EventLogicToGL;

import java.util.EventObject;

public class DeleteMediaEvent extends EventObject {
    private String address;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public DeleteMediaEvent(Object source, String address) {
        super(source);
        this.address = address;
    }

    public String getAddress() { return address; }

}
