package EventSystem.EventLogicToGL;

import java.util.EventObject;

public class ProducerEvent extends EventObject{
    private String name;
    boolean addOrDelete;

    public ProducerEvent(Object source, String name, boolean addOrDelete) {
        super(source);
        this.name = name;
        this.addOrDelete = addOrDelete;
    }

    public String getName() { return name; }
    public boolean getAddOrDelete() {return addOrDelete;}

}
