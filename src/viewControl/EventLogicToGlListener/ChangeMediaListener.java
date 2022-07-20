package viewControl.EventLogicToGlListener;

import EventSystem.ToGL.ChangeMediaEvent;
import EventSystem.ToGL.ChangeMediaEventListener;
import administration.Administration;

public class ChangeMediaListener implements ChangeMediaEventListener {
    Administration administration = null;

    public ChangeMediaListener(Administration administration) {this.administration = administration;}

    @Override
    public void onChangeMediaEvent(ChangeMediaEvent event) {
        this.administration.changeMedia(event.getAddress());
    }
}
