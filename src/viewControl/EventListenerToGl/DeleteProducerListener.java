package viewControl.EventListenerToGl;

import EventSystem.ToGL.EinfuegenUndLoeschenModus.ProducerEvent;
import EventSystem.ToGL.EinfuegenUndLoeschenModus.ProducerEventListener;
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
