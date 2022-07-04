package viewControl.EventLogicListenerToGlListener;

import EventSystem.EventLogicToGL.ProducerEvent;
import EventSystem.EventLogicToGL.ProducerEventListener;
import administration.Administration;

public class AddProducerListener implements ProducerEventListener {
    Administration administration = null;

    public AddProducerListener(Administration administration) {this.administration = administration;}

    @Override
    public void onProducerEvent(ProducerEvent event) {
        this.administration.addProducer(event.getName());
    }
}

