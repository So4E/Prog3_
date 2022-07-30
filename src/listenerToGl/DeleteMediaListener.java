package listenerToGl;

import eventSystem.toGL.einfuegenUndLoeschenModus.DeleteMediaEvent;
import eventSystem.toGL.einfuegenUndLoeschenModus.DeleteMediaEventListener;
import administration.Administration;

public class DeleteMediaListener implements DeleteMediaEventListener {
    Administration administration = null;

    public DeleteMediaListener(Administration administration) {this.administration = administration;}

    @Override
    public void onDeleteMediaEvent(DeleteMediaEvent event) {
        this.administration.deleteMedia(event.getAddress());
    }
}
