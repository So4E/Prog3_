package viewControl.EventLogicToGlListener;

import EventSystem.ToGL.MediaEvent;
import EventSystem.ToGL.MediaEventListener;
import administration.Administration;

public class AddMediaListener implements MediaEventListener {
    Administration administration = null;

    public AddMediaListener(Administration administration) {
        this.administration = administration;
    }

    @Override
    public void onMediaEvent(MediaEvent event) {
        this.administration.addMedia(event.getMediaType(), event.getNameOfProducer(), event.getTags(), event.getBitrate(),
             event.getLength(), event.getOptionaleParameter());

    }
}