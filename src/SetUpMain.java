import EventSystem.EventLogicListenerToGL.*;
import EventSystem.EventLogicToGL.*;
import EventSystem.Observer.Observer;
import EventSystem.Observer.SizeObserver;
import EventSystem.Observer.TagObserver;
import EventSystem.viewControl.Console;
import administration.Administration;
import administration.AdministrationImpl;

import java.math.BigDecimal;

public class SetUpMain {

    public static void main(String[] args){

        //neue Console erzeugen (= im Schaubild: Controller)
        Console console = new Console();

        //GL erstellen mit eingegebenem Parameter für capacity
        Administration administration = new AdministrationImpl(BigDecimal.valueOf(Long.parseLong(args[0])));
        //TODO input ist die capacity und UDP oder TCP -> client server -> input hier noch aufspalten

        //Listener -> an diese als Argument Administration übergeben
        ProducerEventListener addProducerListener = new AddProducerListener(administration);
        ProducerEventListener deleteProducerListener = new DeleteProducerListener(administration);
        AddMediaListener addMediaListener = new AddMediaListener(administration);
        DeleteMediaListener deleteMediaListener = new DeleteMediaListener(administration);
        ChangeMediaListener changeMediaListener = new ChangeMediaListener(administration);

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

        //Console handler übergeben
        console.setAddProducerHandler(addProducerHandler);
        console.setDeleteProducerHandler(deleteProducerHandler);
        console.setAddMediaHandler(addMediaHandler);
        console.setDeleteMediaEventHandler(deleteMediaEventHandler);
        console.setChangeMediaEventHandler(changeMediaEventHandler);

        //SizeObserver erstellen und admin übergeben
        Observer sizeObserver = new SizeObserver((AdministrationImpl) administration);
        Observer tagObserver = new TagObserver((AdministrationImpl) administration);

        console.execute();
    }
}
