package eventSystem.toGL.aendernModus;

import java.util.LinkedList;
import java.util.List;

public class ChangeMediaEventHandler {
    private List<ChangeMediaEventListener> listenerList = new LinkedList<>();
    public void add(ChangeMediaEventListener listener) {
        this.listenerList.add(listener);
    }
    public void remove(ChangeMediaEventListener listener) {
        this.listenerList.remove(listener);
    }

    public void handle(ChangeMediaEvent event){
        for (ChangeMediaEventListener listener : listenerList) {
            listener.onChangeMediaEvent(event);
        }
    }
}
