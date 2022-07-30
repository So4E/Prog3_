package listenerToGl;

import eventSystem.toGL.einfuegenUndLoeschenModus.ProducerEvent;
import eventSystem.toGL.einfuegenUndLoeschenModus.ProducerEventListener;
import administration.Administration;

public class DeleteProducerListener implements ProducerEventListener {
    Administration administration = null;

    public DeleteProducerListener(Administration administration) {this.administration = administration;}

    @Override
    public void onProducerEvent(ProducerEvent event) {
        if(!event.getAddOrDelete()) {
            this.administration.deleteProducer(event.getName());
        }
    }
}
