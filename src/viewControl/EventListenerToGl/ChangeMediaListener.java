package viewControl.EventListenerToGl;

import EventSystem.ToGL.AendernModus.ChangeMediaEvent;
import EventSystem.ToGL.AendernModus.ChangeMediaEventListener;
import administration.Administration;

public class ChangeMediaListener implements ChangeMediaEventListener {
    Administration administration = null;

    public ChangeMediaListener(Administration administration) {this.administration = administration;}

    @Override
    public void onChangeMediaEvent(ChangeMediaEvent event) {
        this.administration.changeMedia(event.getAddress());
    }
}
