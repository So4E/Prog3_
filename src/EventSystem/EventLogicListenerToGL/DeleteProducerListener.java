package EventSystem.EventLogicListenerToGL;

import EventSystem.EventLogicToGL.ProducerEvent;
import EventSystem.EventLogicToGL.ProducerEventListener;
import administration.Administration;

public class DeleteProducerListener implements ProducerEventListener {
    Administration administration = null;

    public DeleteProducerListener(Administration administration) {this.administration = administration;}

    @Override
    public void onProducerEvent(ProducerEvent event) {
        this.administration.deleteProducer(event.getName());
    }
}
