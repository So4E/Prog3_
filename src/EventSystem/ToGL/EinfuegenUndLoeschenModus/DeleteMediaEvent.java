package EventSystem.ToGL.EinfuegenUndLoeschenModus;

import java.util.EventObject;

public class DeleteMediaEvent extends EventObject {
    private String address;

    public DeleteMediaEvent(Object source, String address) {
        super(source);
        this.address = address;
    }

    public String getAddress() { return address; }

}
