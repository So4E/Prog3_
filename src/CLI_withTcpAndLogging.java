import EventSystem.FromGL.*;
import Observer_InversionOfControl.Observer;
import Observer_InversionOfControl.SizeObserver;
import Observer_InversionOfControl.TagObserver;
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
import ReturnFromGlListener.ShowingMediaListListener;
import ReturnFromGlListener.ShowingProducerListListener;
import ReturnFromGlListener.ShowingTagsListener;
import TCP_Client_Server_ListenerAndHandler.ClientStreamHandler;
import TCP_Client_Server_ListenerAndHandler.ClientStreamListener;
import administration.AdministrationImpl;
import viewControl.ConsoleCLI;
import viewControl.ConsoleCLIWithTCP;
import viewControl.EventListenerToGl.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;

public class CLI_withTcpAndLogging {

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
                ClientStreamListener clientStreamListener = null;
                ClientStreamHandler clientStreamHandler = null;
                ObjectInputStream in = null;

                try {
                    Socket socket = new Socket("localhost", 7005);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    in = new ObjectInputStream(socket.getInputStream());
                    clientStreamListener = new ClientStreamListener(out);
                    clientStreamHandler = new ClientStreamHandler(in);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (clientStreamListener == null) {
                    throw new IllegalStateException("Stream Listener is null");
                }

                ConsoleCLIWithTCP console = new ConsoleCLIWithTCP(clientStreamHandler, in);

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

                //TO DO SaveWithJBP Handler & LoadWithJBP Handler

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

                //TO DO ------------ Loading with JBP


                console.execute();
            }

            if (args[0].equals("UDP")) {
                //TO DO implement UDP Client
            }

            if(args[0].equals("DE") || args[0].equals("EN")){
                String language = args[0];
                //check if additional capacity is given and use if it is there to initialize
                int newCapacity = defaultCapacity.intValue();
                try {
                    newCapacity = Integer.parseInt(args[1]);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                defaultCapacity = new BigDecimal(newCapacity);
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
        AdministrationImpl administration = new AdministrationImpl(capacity);

        //ONE WAY EVENTS -----------------------------------
        //Listener to GL -> Argument Administration
        ProducerEventListener addProducerListener = new AddProducerListener(administration);
        ProducerEventListener deleteProducerListener = new DeleteProducerListener(administration);
        AddMediaListener addMediaListener = new AddMediaListener(administration);
        DeleteMediaListener deleteMediaListener = new DeleteMediaListener(administration);
        ChangeMediaListener changeMediaListener = new ChangeMediaListener(administration);
        SaveWithJOSEventListener saveWithJOSListener = new SaveWithJOSListener(administration);
        //TO DO SaveWithJBPEventListener

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

        //TO DO SaveWithJBP

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

        //TO DO -> Loading with JBP

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
        Observer sizeObserver = new SizeObserver(administration, System.out);
        Observer tagObserver = new TagObserver(administration);

        console.execute();
    }

    static void startCLISetupWithLog(BigDecimal capacity, String language) {
        //create console (= is controller in lecture diagram)
        ConsoleCLI console = new ConsoleCLI();

        //initialize GL with default parameter for capacity
        AdministrationImpl administration = new AdministrationImpl(capacity);

        //get Logger
        Logger logger = Logger.getLogger(language, administration);

        //ONE WAY EVENTS -----------------------------------
        //Listener to GL -> Argument Administration
        ProducerEventListener addProducerListener = new AddProducerListener(administration);
        ProducerEventListener deleteProducerListener = new DeleteProducerListener(administration);
        AddMediaListener addMediaListener = new AddMediaListener(administration);
        DeleteMediaListener deleteMediaListener = new DeleteMediaListener(administration);
        ChangeMediaListener changeMediaListener = new ChangeMediaListener(administration);
        SaveWithJOSEventListener saveWithJOSListener = new SaveWithJOSListener(administration);
        //TO DO SaveWithJBPEventListener

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

        //TO DO SaveWithJBP

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

        //TO DO -> Loading with JBP

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
        Observer sizeObserver = new SizeObserver(administration, System.out);
        Observer tagObserver = new TagObserver(administration);

        console.execute();
    }
}
