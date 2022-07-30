package eventSystem.toGL.einfuegenUndLoeschenModus;

import java.util.EventListener;

public interface ProducerEventListener extends EventListener {
        void onProducerEvent(ProducerEvent event);

}
