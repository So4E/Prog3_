package EventSystem.EventLogicToGL;

import java.util.LinkedList;
import java.util.List;

public class DeleteMediaEventHandler {
    private List<DeleteMediaEventListener> listenerList = new LinkedList<>();
    public void add(DeleteMediaEventListener listener) {
        this.listenerList.add(listener);
    }
    public void remove(DeleteMediaEventListener listener) {
        this.listenerList.remove(listener);
    }

    public void handle(DeleteMediaEvent event){
        for (DeleteMediaEventListener listener : listenerList) {
            listener.onDeleteMediaEvent(event);
        }
    }
}
