package eventSystem.toGL.einfuegenUndLoeschenModus;

import java.util.EventListener;

public interface MediaEventListener extends EventListener {
    void onMediaEvent(MediaEvent event);
}
