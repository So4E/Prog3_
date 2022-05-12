package EventSystem.EventLogicListenerToGL;

import EventSystem.EventLogicToGL.DeleteMediaEvent;
import EventSystem.EventLogicToGL.DeleteMediaEventListener;
import administration.Administration;

public class DeleteMediaListener implements DeleteMediaEventListener {
    Administration administration = null;

    public DeleteMediaListener(Administration administration) {this.administration = administration;}

    @Override
    public void onDeleteMediaEvent(DeleteMediaEvent event) {
        this.administration.deleteMedia(event.getAddress());
    }
}
