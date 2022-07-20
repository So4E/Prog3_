package EventSystem.ToGL;

import java.util.EventListener;

public interface ChangeMediaEventListener extends EventListener {
    void onChangeMediaEvent(ChangeMediaEvent event);
}
