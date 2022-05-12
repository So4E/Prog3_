package EventSystem.EventLogicToGL;

import java.util.EventObject;

public class ProducerEvent extends EventObject{
    private String name;

    public ProducerEvent(Object source, String name) {
        super(source);
        this.name = name;
    }

    public String getName() { return name; }

}
