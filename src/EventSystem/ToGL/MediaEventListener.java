package EventSystem.ToGL;

import java.util.EventListener;

public interface MediaEventListener extends EventListener {
    void onMediaEvent(MediaEvent event);
}
