package EventSystem.ToGL;

import java.util.LinkedList;
import java.util.List;

public class ProducerEventHandler {
    private List<ProducerEventListener> listenerList = new LinkedList<>();
    public void add(ProducerEventListener listener) {
        this.listenerList.add(listener);
    }
    public void remove(ProducerEventListener listener) {
        this.listenerList.remove(listener);
    }

    public void handle(ProducerEvent event) {
        for (ProducerEventListener listener : listenerList) {
            listener.onProducerEvent(event);
        }
    }
}
