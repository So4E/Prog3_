package EventSystem.EventLogicToGL;

import java.util.LinkedList;
import java.util.List;

public class MediaEventHandler {
    private List<MediaEventListener> listenerList = new LinkedList<>();

    public void add(MediaEventListener listener) {
        this.listenerList.add(listener);
    }

    public void remove(MediaEventListener listener) {
        this.listenerList.remove(listener);
    }

    public void handle(MediaEvent event) {
        for (MediaEventListener listener : listenerList) {
            listener.onMediaEvent(event);
        }
    }
}
