package eventSystem.toGL.einfuegenUndLoeschenModus;

import java.util.EventListener;

public interface DeleteMediaEventListener extends EventListener {
    void onDeleteMediaEvent(DeleteMediaEvent event);
}
