package EventSystem.ToGL;

import java.util.EventListener;

public interface ProducerEventListener extends EventListener {
        void onProducerEvent(ProducerEvent event);

}
