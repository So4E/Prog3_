package EventSystem.EventLogicToGL;

import java.util.EventListener;

public interface DeleteMediaEventListener extends EventListener {
    void onDeleteMediaEvent(DeleteMediaEvent event);
}
