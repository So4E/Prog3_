import EventSystem.FromGL.*;
import EventSystem.ToGL.AendernModus.ChangeMediaEventHandler;
import EventSystem.ToGL.Anzeigenmodus.*;
import EventSystem.ToGL.EinfuegenUndLoeschenModus.DeleteMediaEventHandler;
import EventSystem.ToGL.EinfuegenUndLoeschenModus.MediaEventHandler;
import EventSystem.ToGL.EinfuegenUndLoeschenModus.ProducerEventHandler;
import EventSystem.ToGL.EinfuegenUndLoeschenModus.ProducerEventListener;
import EventSystem.ToGL.Persistenzmodus.LoadWithJOSEventHandler;
import EventSystem.ToGL.Persistenzmodus.LoadWithJOSEventListener;
import EventSystem.ToGL.Persistenzmodus.SaveWithJOSEventHandler;
import EventSystem.ToGL.Persistenzmodus.SaveWithJOSEventListener;
import Logging.Logger;
import ReturnFromGlListener.LoadingJOSListener;
import ReturnFromGlListener.ShowingProducerListListener;
import ReturnFromGlListener.ShowingTagsListener;
import TCP_Client_Server_ListenerAndHandler.ClientStreamHandler;
import TCP_Client_Server_ListenerAndHandler.ClientStreamListener;
import EventSystem.Observer_InversionOfControl.Observer;
import EventSystem.Observer_InversionOfControl.SizeObserver;
import EventSystem.Observer_InversionOfControl.TagObserver;
import administration.Administration;
import administration.AdministrationImpl;
import viewControl.ConsoleCLI;
import ReturnFromGlListener.ShowingMediaListListener;
import viewControl.EventLogicToGlListener.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;

public class CLI_withTCPClient {

    //arguments here can be TCP, UDP, capacity for admin, or nothing
    public static void main(String[] args) {
        BigDecimal defaultCapacity = new BigDecimal(10000);

        //case no arguments are given, start CLISetup with default capacity
        if (args.length == 0) {
            startCLISetup(defaultCapacity);
        }

        //case arguments are given
        if (args.length > 0) {
            //args auslesen: capacity oder TCP/UDP = args[0]
            if (args[0].equals("TCP")) {
                ConsoleCLI console = new ConsoleCLI();
                ClientStreamListener clientStreamListener = null;
                ClientStreamHandler clientStreamHandler = null;

                try {
                    Socket socket = new Socket("localhost", 7005);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    clientStreamListener = new ClientStreamListener(out);
                    clientStreamHandler = new ClientStreamHandler(in);
                    //out.writeChar('P');
                    //out.flush();
                    //TODO hier write und flush(); noch weg

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (clientStreamListener == null) {
                    throw new IllegalStateException("Stream Listener is null");
                }

                //Handler erstellen -> an diese den StreamListener als Argument übergeben
                ProducerEventHandler addProducerHandler = new ProducerEventHandler();
                addProducerHandler.add(clientStreamListener);

                ProducerEventHandler deleteProducerHandler = new ProducerEventHandler();
                deleteProducerHandler.add(clientStreamListener);

                MediaEventHandler addMediaHandler = new MediaEventHandler();
                addMediaHandler.add(clientStreamListener);

                DeleteMediaEventHandler deleteMediaEventHandler = new DeleteMediaEventHandler();
                deleteMediaEventHandler.add(clientStreamListener);

                ChangeMediaEventHandler changeMediaEventHandler = new ChangeMediaEventHandler();
                changeMediaEventHandler.add(clientStreamListener);

                SaveWithJOSEventHandler saveWithJOSEventHandler = new SaveWithJOSEventHandler();
                saveWithJOSEventHandler.add(clientStreamListener);

                LoadWithJOSEventHandler loadWithJOSEventHandler = new LoadWithJOSEventHandler();
                loadWithJOSEventHandler.add(clientStreamListener);

                //TODO SaveWithJBP Handler & LoadWithJBP Handler

                ShowMediaListEventHandler showMediaListEventHandler = new ShowMediaListEventHandler();
                showMediaListEventHandler.add(clientStreamListener);

                ShowTagsEventHandler showTagsEventHandler = new ShowTagsEventHandler();
                showTagsEventHandler.add(clientStreamListener);

                ShowProducerListEventHandler showProducerListEventHandler = new ShowProducerListEventHandler();
                showProducerListEventHandler.add(clientStreamListener);

                //Console handler übergeben
                console.setAddProducerHandler(addProducerHandler);
                console.setDeleteProducerHandler(deleteProducerHandler);
                console.setAddMediaHandler(addMediaHandler);
                console.setDeleteMediaEventHandler(deleteMediaEventHandler);
                console.setChangeMediaEventHandler(changeMediaEventHandler);

                console.setSaveWithJOSEventHandler(saveWithJOSEventHandler);
                console.setLoadWithJOSEventHandler(loadWithJOSEventHandler);

                console.setShowMediaListEventHandler(showMediaListEventHandler);
                console.setShowTagsEventHandler(showTagsEventHandler);
                console.setShowProducerListEventHandler(showProducerListEventHandler);

                //Return way -> Answer from Server
                //----------- Showing Media List
                ShowingMediaListEventListener showingMediaListListener = new ShowingMediaListListener(console);
                ShowingMediaListEventHandler showingMediaListEventHandler = new ShowingMediaListEventHandler();
                showingMediaListEventHandler.add(showingMediaListListener);
                clientStreamHandler.setShowingMediaListEventHandler(showingMediaListEventHandler);

                //----------- Showing Producer List
                ShowingProducerListEventListener showingProducerListListener = new ShowingProducerListListener(console);
                ShowingProducerListEventHandler showingProducerListEventHandler = new ShowingProducerListEventHandler();
                showingProducerListEventHandler.add(showingProducerListListener);
                clientStreamHandler.setShowingProducerListEventHandler(showingProducerListEventHandler);

                //----------- Showing Tags
                ShowingTagsEventListener showingTagsListener = new ShowingTagsListener(console);
                ShowingTagsEventHandler showingTagsEventHandler = new ShowingTagsEventHandler();
                showingTagsEventHandler.add(showingTagsListener);
                clientStreamHandler.setShowingTagsEventHandler(showingTagsEventHandler);

                //----------- Loading with JOS
                LoadingJOSEventListener loadingJOSListener = new LoadingJOSListener(console);
                LoadingJOSEventHandler loadingJOSEventHandler = new LoadingJOSEventHandler();
                loadingJOSEventHandler.add(loadingJOSListener);
                clientStreamHandler.setLoadingJOSEventHandler(loadingJOSEventHandler);

                //TODO ------------ Loading with JBP

                while (true) {
                    console.execute();
                }
            }

            if (args[0].equals("UDP")) {
                //TODO implement UDP Client
            }

            if(args[0].equals("DT") || args[0].equals("EN")){
                String language = args[0];
                //initialize cli with default capacity and language for logging
                startCLISetupWithLog(defaultCapacity, language);
            }

            //case args[0] is not TCP, UDP, nor DT or EN -> it is capacity
            else {
                //default int according to defaultCapacity set at beginning
                int newCapacity = defaultCapacity.intValue();
                try {
                    //try to parse input to an int
                    newCapacity = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                //any way initialize cli with default or set capacity
                defaultCapacity = new BigDecimal(newCapacity);
                startCLISetup(defaultCapacity);
            }
        }
    }


    static void startCLISetup(BigDecimal capacity) {

        //create console (= is controller in lecture diagram)
        ConsoleCLI console = new ConsoleCLI();

        //initialize GL with default parameter for capacity
        Administration administration = new AdministrationImpl(capacity);

        //ONE WAY EVENTS -----------------------------------
        //Listener to GL -> Argument Administration
        ProducerEventListener addProducerListener = new AddProducerListener(administration);
        ProducerEventListener deleteProducerListener = new DeleteProducerListener(administration);
        AddMediaListener addMediaListener = new AddMediaListener(administration);
        DeleteMediaListener deleteMediaListener = new DeleteMediaListener(administration);
        ChangeMediaListener changeMediaListener = new ChangeMediaListener(administration);
        SaveWithJOSEventListener saveWithJOSListener = new SaveWithJOSListener(administration);
        //TODO SaveWithJBPEventListener

        //One Way Handler -> Argument Listener
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

        SaveWithJOSEventHandler saveWithJOSEventHandler = new SaveWithJOSEventHandler();
        saveWithJOSEventHandler.add(saveWithJOSListener);

        //TODO SaveWithJBP

        //EVENTS WITH ANSWER ----------------------------------------
        // EVENT -> Showing Media List -----------
        // From GL -> return
        ShowingMediaListEventHandler showingMediaListEventHandler = new ShowingMediaListEventHandler();
        ShowingMediaListListener mediaListListener = new ShowingMediaListListener(console);
        showingMediaListEventHandler.add(mediaListListener);
        //To GL -> initial way
        ShowMediaListEventHandler showMediaListEventHandler = new ShowMediaListEventHandler();
        ShowMediaListEventListener showMediaListEventListener = new ShowMediaListListener(administration, showingMediaListEventHandler);
        showMediaListEventHandler.add(showMediaListEventListener);
        //____________________________ENDE Show Media List

        //EVENT -> Showing Producer List -------------
        //From GL
        ShowingProducerListEventHandler showingProducerListEventHandler = new ShowingProducerListEventHandler();
        ShowingProducerListListener showingProducerListListener = new ShowingProducerListListener(console);
        showingProducerListEventHandler.add(showingProducerListListener);
        //To GL
        ShowProducerListEventHandler showProducerListEventHandler = new ShowProducerListEventHandler();
        ShowProducerListEventListener showProducerListListener = new ShowProducerListListener(administration, showingProducerListEventHandler);
        showProducerListEventHandler.add(showProducerListListener);
        //---------------------------Ende Show Producer List

        //EVENT -> Showing Tags -------------
        //From GL
        ShowingTagsEventHandler showingTagsEventHandler = new ShowingTagsEventHandler();
        ShowingTagsListener showingTagsListener = new ShowingTagsListener(console);
        showingTagsEventHandler.add(showingTagsListener);
        //To GL
        ShowTagsEventHandler showTagsEventHandler = new ShowTagsEventHandler();
        ShowTagsEventListener showTagsListener = new ShowTagsListener(administration, showingTagsEventHandler);
        showTagsEventHandler.add(showTagsListener);
        //-------------------------- Ende Show Tags

        //EVENT -> Loading with JOS --------------------
        //From GL
        LoadingJOSEventHandler loadingJOSEventHandler = new LoadingJOSEventHandler();
        LoadingJOSEventListener loadingJOSListener = new LoadingJOSListener(console);
        loadingJOSEventHandler.add(loadingJOSListener);
        //To GL
        LoadWithJOSEventHandler loadWithJOSEventHandler = new LoadWithJOSEventHandler();
        LoadWithJOSEventListener loadWithJOSListener = new LoadWithJOSListener(administration, loadingJOSEventHandler);
        loadWithJOSEventHandler.add(loadWithJOSListener);
        //----------------ENDE JOS LADEN Config.

        //Todo -> Loading with JBP

        //console -> set handler for input handling
        console.setAddProducerHandler(addProducerHandler);
        console.setDeleteProducerHandler(deleteProducerHandler);
        console.setAddMediaHandler(addMediaHandler);
        console.setDeleteMediaEventHandler(deleteMediaEventHandler);
        console.setChangeMediaEventHandler(changeMediaEventHandler);

        console.setShowMediaListEventHandler(showMediaListEventHandler);
        console.setShowProducerListEventHandler(showProducerListEventHandler);
        console.setShowTagsEventHandler(showTagsEventHandler);

        console.setSaveWithJOSEventHandler(saveWithJOSEventHandler);
        console.setLoadWithJOSEventHandler(loadWithJOSEventHandler);

        //create observer -> Argument administration
        Observer sizeObserver = new SizeObserver((AdministrationImpl) administration);
        Observer tagObserver = new TagObserver((AdministrationImpl) administration);

        console.execute();
    }

    static void startCLISetupWithLog(BigDecimal capacity, String language) {
        Logger logger = Logger.getLogger(language);
        //create console (= is controller in lecture diagram)
        ConsoleCLI console = new ConsoleCLI();

        //initialize GL with default parameter for capacity
        Administration administration = new AdministrationImpl(capacity);

        //ONE WAY EVENTS -----------------------------------
        //Listener to GL -> Argument Administration
        ProducerEventListener addProducerListener = new AddProducerListener(administration);
        ProducerEventListener deleteProducerListener = new DeleteProducerListener(administration);
        AddMediaListener addMediaListener = new AddMediaListener(administration);
        DeleteMediaListener deleteMediaListener = new DeleteMediaListener(administration);
        ChangeMediaListener changeMediaListener = new ChangeMediaListener(administration);
        SaveWithJOSEventListener saveWithJOSListener = new SaveWithJOSListener(administration);
        //TODO SaveWithJBPEventListener

        //One Way Handler -> Argument Listener
        ProducerEventHandler addProducerHandler = new ProducerEventHandler();
        addProducerHandler.add(addProducerListener);
        addProducerHandler.add(logger);

        ProducerEventHandler deleteProducerHandler = new ProducerEventHandler();
        deleteProducerHandler.add(deleteProducerListener);
        deleteProducerHandler.add(logger);

        MediaEventHandler addMediaHandler = new MediaEventHandler();
        addMediaHandler.add(addMediaListener);
        addMediaHandler.add(logger);

        DeleteMediaEventHandler deleteMediaEventHandler = new DeleteMediaEventHandler();
        deleteMediaEventHandler.add(deleteMediaListener);
        deleteMediaEventHandler.add(logger);

        ChangeMediaEventHandler changeMediaEventHandler = new ChangeMediaEventHandler();
        changeMediaEventHandler.add(changeMediaListener);
        changeMediaEventHandler.add(logger);

        SaveWithJOSEventHandler saveWithJOSEventHandler = new SaveWithJOSEventHandler();
        saveWithJOSEventHandler.add(saveWithJOSListener);
        saveWithJOSEventHandler.add(logger);

        //TODO SaveWithJBP

        //EVENTS WITH ANSWER ----------------------------------------
        // EVENT -> Showing Media List -----------
        // From GL -> return
        ShowingMediaListEventHandler showingMediaListEventHandler = new ShowingMediaListEventHandler();
        ShowingMediaListListener mediaListListener = new ShowingMediaListListener(console);
        showingMediaListEventHandler.add(mediaListListener);
        //To GL -> initial way
        ShowMediaListEventHandler showMediaListEventHandler = new ShowMediaListEventHandler();
        ShowMediaListEventListener showMediaListEventListener = new ShowMediaListListener(administration, showingMediaListEventHandler);
        showMediaListEventHandler.add(showMediaListEventListener);
        showMediaListEventHandler.add(logger);
        //____________________________ENDE Show Media List

        //EVENT -> Showing Producer List -------------
        //From GL
        ShowingProducerListEventHandler showingProducerListEventHandler = new ShowingProducerListEventHandler();
        ShowingProducerListListener showingProducerListListener = new ShowingProducerListListener(console);
        showingProducerListEventHandler.add(showingProducerListListener);
        //To GL
        ShowProducerListEventHandler showProducerListEventHandler = new ShowProducerListEventHandler();
        ShowProducerListEventListener showProducerListListener = new ShowProducerListListener(administration, showingProducerListEventHandler);
        showProducerListEventHandler.add(showProducerListListener);
        showProducerListEventHandler.add(logger);
        //---------------------------Ende Show Producer List

        //EVENT -> Showing Tags -------------
        //From GL
        ShowingTagsEventHandler showingTagsEventHandler = new ShowingTagsEventHandler();
        ShowingTagsListener showingTagsListener = new ShowingTagsListener(console);
        showingTagsEventHandler.add(showingTagsListener);
        //To GL
        ShowTagsEventHandler showTagsEventHandler = new ShowTagsEventHandler();
        ShowTagsEventListener showTagsListener = new ShowTagsListener(administration, showingTagsEventHandler);
        showTagsEventHandler.add(showTagsListener);
        showTagsEventHandler.add(logger);
        //-------------------------- Ende Show Tags

        //EVENT -> Loading with JOS --------------------
        //From GL
        LoadingJOSEventHandler loadingJOSEventHandler = new LoadingJOSEventHandler();
        LoadingJOSEventListener loadingJOSListener = new LoadingJOSListener(console);
        loadingJOSEventHandler.add(loadingJOSListener);
        //To GL
        LoadWithJOSEventHandler loadWithJOSEventHandler = new LoadWithJOSEventHandler();
        LoadWithJOSEventListener loadWithJOSListener = new LoadWithJOSListener(administration, loadingJOSEventHandler);
        loadWithJOSEventHandler.add(loadWithJOSListener);
        loadWithJOSEventHandler.add(logger);
        //----------------ENDE JOS LADEN Config.

        //Todo -> Loading with JBP

        //console -> set handler for input handling
        console.setAddProducerHandler(addProducerHandler);
        console.setDeleteProducerHandler(deleteProducerHandler);
        console.setAddMediaHandler(addMediaHandler);
        console.setDeleteMediaEventHandler(deleteMediaEventHandler);
        console.setChangeMediaEventHandler(changeMediaEventHandler);

        console.setShowMediaListEventHandler(showMediaListEventHandler);
        console.setShowProducerListEventHandler(showProducerListEventHandler);
        console.setShowTagsEventHandler(showTagsEventHandler);

        console.setSaveWithJOSEventHandler(saveWithJOSEventHandler);
        console.setLoadWithJOSEventHandler(loadWithJOSEventHandler);

        //TODO - hier boolean an sizeObserver, ob geloggt oder nich tübergeben?
        //create observer -> Argument administration
        Observer sizeObserver = new SizeObserver((AdministrationImpl) administration);
        Observer tagObserver = new TagObserver((AdministrationImpl) administration);

        console.execute();
    }
}
