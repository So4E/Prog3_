package listenerToGl;

import eventSystem.toGL.einfuegenUndLoeschenModus.ProducerEvent;
import eventSystem.toGL.einfuegenUndLoeschenModus.ProducerEventListener;
import administration.Administration;

public class AddProducerListener implements ProducerEventListener {
    Administration administration = null;

    public AddProducerListener(Administration administration) {this.administration = administration;}

    @Override
    public void onProducerEvent(ProducerEvent event) {
        if(event.getAddOrDelete()) {
            this.administration.addProducer(event.getName());
        }
    }
}

