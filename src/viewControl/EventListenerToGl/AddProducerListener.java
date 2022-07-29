package viewControl.EventListenerToGl;

import EventSystem.ToGL.EinfuegenUndLoeschenModus.ProducerEvent;
import EventSystem.ToGL.EinfuegenUndLoeschenModus.ProducerEventListener;
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

