import EventSystem.EventLogicFromGL.MediaListEventHandler;
import EventSystem.EventLogicFromGL.MediaListListener;
import EventSystem.EventLogicFromGL.ShowMediaListListener;
import EventSystem.EventLogicListenerToGL.*;
import EventSystem.EventLogicToGL.*;
import EventSystem.viewControl.*;
import EventSystem.Observer.Observer;
import EventSystem.Observer.SizeObserver;
import EventSystem.Observer.TagObserver;
import administration.Administration;
import administration.AdministrationImpl;

import java.math.BigDecimal;

public class SetUpCLI {

    public static void main(String[] args){

        //neue Console erzeugen (= im Schaubild: Controller)
        ConsoleCLI console = new ConsoleCLI();

        //GL erstellen mit eingegebenem Parameter für capacity
        Administration administration = new AdministrationImpl(BigDecimal.valueOf(Long.parseLong(args[0])));
        //TODO input ist die capacity und UDP oder TCP -> client server -> input hier noch aufspalten

        //Listener richtung GL -> an diese als Argument Administration übergeben
        ProducerEventListener addProducerListener = new AddProducerListener(administration);
        ProducerEventListener deleteProducerListener = new DeleteProducerListener(administration);
        AddMediaListener addMediaListener = new AddMediaListener(administration);
        DeleteMediaListener deleteMediaListener = new DeleteMediaListener(administration);
        ChangeMediaListener changeMediaListener = new ChangeMediaListener(administration);
        ShowMediaListEventListener showMediaListEventListener = new ShowMediaListListener(administration);

        //Listener Richtung Console -> an diese Console als Argument übergeben
        MediaListListener mediaListListener = new MediaListListener(console);

        //Handler erstellen -> an diese Listener als Argumente übergeben
        ProducerEventHandler addProducerHandler = new ProducerEventHandler();
        addProducerHandler.add(addProducerListener);

        ProducerEventHandler deleteProducerHandler = new ProducerEventHandler();
        deleteProducerHandler.add(deleteProducerListener);

        MediaEventHandler addMediaHandler = new MediaEventHandler();
        addMediaHandler.add(addMediaListener);

        DeleteMediaEventHandler deleteMediaEventHandler = new DeleteMediaEventHandler();
        deleteMediaEventHandler.add(deleteMediaListener);

        ChangeMediaEventHandler changeMediaEventHandler = new ChangeMediaEventHandler();
        changeMediaEventHandler.add(changeMediaListener);

        ShowMediaListEventHandler showMediaListEventHandler = new ShowMediaListEventHandler();
        showMediaListEventHandler.add(showMediaListEventListener);

        MediaListEventHandler mediaListEventHandler = new MediaListEventHandler();
        mediaListEventHandler.add(mediaListListener);

        //Console handler übergeben
        console.setAddProducerHandler(addProducerHandler);
        console.setDeleteProducerHandler(deleteProducerHandler);
        console.setAddMediaHandler(addMediaHandler);
        console.setDeleteMediaEventHandler(deleteMediaEventHandler);
        console.setChangeMediaEventHandler(changeMediaEventHandler);
        console.setShowMediaListEventHandler(showMediaListEventHandler);
        //hier noch MediaListEventHandler übergeben? -> muss console eigentlich nicht kennen, oder?

        //SizeObserver erstellen und admin übergeben
        Observer sizeObserver = new SizeObserver((AdministrationImpl) administration);
        Observer tagObserver = new TagObserver((AdministrationImpl) administration);

        console.execute();
    }
}
