package EventSystem.EventLogicToGL;

import java.util.EventListener;

public interface ProducerEventListener extends EventListener {
        void onProducerEvent(ProducerEvent event);

}
