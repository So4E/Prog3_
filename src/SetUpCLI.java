import Client_Server_Listener_und_Handler.ClientStreamListener;
import Client_Server_Listener_und_Handler.ServerStreamHandlerToGL;
import EventSystem.EventLogicFromGL.MediaListEventHandler;
import EventSystem.EventLogicToGL.*;
import EventSystem.Observer.Observer;
import EventSystem.Observer.SizeObserver;
import EventSystem.Observer.TagObserver;
import administration.Administration;
import administration.AdministrationImpl;
import viewControl.ConsoleCLI;
import viewControl.EventLogicFromGlListener.MediaListListener;
import viewControl.EventLogicListenerToGlListener.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;

public class SetUpCLI {

    //arguments here can be TCP, UDP, capacity for admin, or nothing
    public static void main(String[] args) {
        BigDecimal defaultCapacity = new BigDecimal(10000);

        if (args.length > 0) {
            //args auslesen: capacity oder TCP/UDP = args[0]
            //wenn TCP oder UDP -> neuer listener in alle alten handler einhängen, der events serialisiert in
            //  Stream schreibt
            if (args[0].equals("TCP")) {

                ConsoleCLI console = new ConsoleCLI();
                ClientStreamListener streamListener = null;
                ServerStreamHandlerToGL streamHandler = null;

                //client
                try {
                    Socket socket = new Socket("localhost", 7005);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    streamListener = new ClientStreamListener(out);
                    streamHandler = new ServerStreamHandlerToGL(in);
                    //out.writeChar('P');
                    //out.flush();
                    //TODO hier write und flush(); noch weg

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (streamListener == null) {
                    throw new IllegalStateException("Stream Listener is null");
                }

                //Handler erstellen -> an diese den StreamListener als Argument übergeben
                ProducerEventHandler addProducerHandler = new ProducerEventHandler();
                addProducerHandler.add(streamListener);

                ProducerEventHandler deleteProducerHandler = new ProducerEventHandler();
                deleteProducerHandler.add(streamListener);

                MediaEventHandler addMediaHandler = new MediaEventHandler();
                addMediaHandler.add(streamListener);

                DeleteMediaEventHandler deleteMediaEventHandler = new DeleteMediaEventHandler();
                deleteMediaEventHandler.add(streamListener);

                ChangeMediaEventHandler changeMediaEventHandler = new ChangeMediaEventHandler();
                changeMediaEventHandler.add(streamListener);

                ShowMediaListEventHandler showMediaListEventHandler = new ShowMediaListEventHandler();
                showMediaListEventHandler.add(streamListener);

                //Console handler übergeben
                console.setAddProducerHandler(addProducerHandler);
                console.setDeleteProducerHandler(deleteProducerHandler);
                console.setAddMediaHandler(addMediaHandler);
                console.setDeleteMediaEventHandler(deleteMediaEventHandler);
                console.setChangeMediaEventHandler(changeMediaEventHandler);
                console.setShowMediaListEventHandler(showMediaListEventHandler);

                while (true) {
                    console.execute();
                }
            }

            if (args[0].equals("UDP")) {
                //TODO hier UDP Client implementieren
            }

            //case args[0] is not TCP or UDP it is capacity, so save instead of default capacity for use
            //and start CLI with this new Capacity
            try {
                int newCapacity = Integer.parseInt(args[0]);
                defaultCapacity = new BigDecimal(newCapacity);
                startCLISetup(defaultCapacity);
            } catch (NumberFormatException e) {
            }
        }

        //case no arguments are given, start CLISetup with default capacity
        if (args.length == 0) {
            startCLISetup(defaultCapacity);
        }
    }


    static void startCLISetup(BigDecimal capacity) {
        //neue Console erzeugen (= im Schaubild: Controller)
        ConsoleCLI console = new ConsoleCLI();

        //GL erstellen mit default Parameter für capacity
        Administration administration = new AdministrationImpl(capacity);

        //Listener richtung GL -> an diese als Argument Administration übergeben
        ProducerEventListener addProducerListener = new AddProducerListener(administration);
        ProducerEventListener deleteProducerListener = new DeleteProducerListener(administration);
        AddMediaListener addMediaListener = new AddMediaListener(administration);
        DeleteMediaListener deleteMediaListener = new DeleteMediaListener(administration);
        ChangeMediaListener changeMediaListener = new ChangeMediaListener(administration);

        //EVENT MIT RÜCKANTWORT -> MEDIALISTE HOLEN
        //Handler und Listener mit Rückweg
        //Handler für Hinweg
        ShowMediaListEventHandler showMediaListEventHandler = new ShowMediaListEventHandler();

        // Listener für Hinweg -> braucht handler für Rückweg:
        // Handler für Rückweg
        MediaListEventHandler mediaListEventHandler = new MediaListEventHandler();
        //Listener für Hinweg
        ShowMediaListEventListener showMediaListEventListener = new ShowMediaListListener(administration, mediaListEventHandler);

        //Handler für Hinweg Listener übergeben
        showMediaListEventHandler.add(showMediaListEventListener);

        //Listener für Rückweg -> muss Console kennen
        //(Listener Richtung Console -> an diese Console als Argument übergeben)
        MediaListListener mediaListListener = new MediaListListener(console);

        //Handler für Rückweg bekommt Listener für Rückweg, der auf Console ausgibt
        mediaListEventHandler.add(mediaListListener);
        //____________________________-ENDE EVENT MIT RÜCKANTWORT

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
        console.setShowMediaListEventHandler(showMediaListEventHandler);

        //SizeObserver erstellen und admin übergeben
        Observer sizeObserver = new SizeObserver((AdministrationImpl) administration);
        Observer tagObserver = new TagObserver((AdministrationImpl) administration);

        console.execute();
    }
}
