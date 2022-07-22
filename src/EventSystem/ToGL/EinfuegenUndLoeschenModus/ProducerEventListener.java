package EventSystem.ToGL.EinfuegenUndLoeschenModus;

import java.util.EventListener;

public interface ProducerEventListener extends EventListener {
        void onProducerEvent(ProducerEvent event);

}
