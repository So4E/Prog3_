package EventSystem.viewControl;

import EventSystem.EventLogicToGL.*;
import mediaDB.Tag;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

import static EventSystem.EventLogicToGL.ProducerEventListener.Modus.*;

public class Console {
    public ProducerEventListener.Modus modus = START;

    //strings to decide which kind of media is to be uploaded/ changed
    public static final String AudioVideo = "audiovideo";
    public static final String Audio = "audio";
    public static final String Video = "video";
    public static final String InteractiveVideo = "interactivevideo";
    public static final String LicencedAudio = "licencedaudio";
    public static final String LicencedVideo = "licencedvideo";
    public static final String LicencedAudioVideo = "licencedaudiovideo";


    //------------ Producer Event Handler
    private ProducerEventHandler addProducerHandler, deleteProducerHandler;

    public void setAddProducerHandler(ProducerEventHandler handler) {
        this.addProducerHandler = handler;
    }

    public void setDeleteProducerHandler(ProducerEventHandler handler) {
        this.deleteProducerHandler = handler;
    }


    //------------- MediaEvent Handler (add Media)
    private MediaEventHandler addMediaHandler;

    public void setAddMediaHandler(MediaEventHandler handler) {
        this.addMediaHandler = handler;
    }


    //------------- DeleteMediaEvent Handler
    private DeleteMediaEventHandler deleteMediaEventHandler;

    public void setDeleteMediaEventHandler(DeleteMediaEventHandler handler) {
        this.deleteMediaEventHandler = handler;
    }

    //------------- ChangeMediaEvent Handler
    private  ChangeMediaEventHandler changeMediaEventHandler;
    public void setChangeMediaEventHandler(ChangeMediaEventHandler changeMediaEventHandler) {
        this.changeMediaEventHandler = changeMediaEventHandler;
    }

    public void execute() {
        try (Scanner s = new Scanner(System.in)) {
            do {
                try {
                    switch (modus) {
                        case START:
                            System.out.println("\nWelcome. \nPlease choose first mode from the following options: \n" +
                                    ":c Wechsel in den Einfügemodus\n" +
                                    ":d Wechsel in den Löschmodus\n" +
                                    ":r Wechsel in den Anzeigemodus\n" +
                                    ":u Wechsel in den Änderungsmodus\n" +
                                    ":p Wechsel in den Persistenzmodus\n" +
                                    "Please enter mode: ");
                            String input = s.next();
                            //first check for commandMode input
                            if (input.charAt(0) == ':') {
                                CommandMode commandMode = new CommandMode(input);
                                //set this mode if default was not used in commandMode
                                if (commandMode.defaultModeUsed != true) {
                                    this.modus = commandMode.modus;
                                }
                            } else {
                                System.out.println("No valid input. Please try again.");
                            }

                        case EINFUEGEMODUS:

                            System.out.println("enter command: ");
                            input = s.next();
                            if (input.contains(":")) {
                                CommandMode commandMode = new CommandMode(input);
                                //set this mode if default was not used in commandMode
                                if (commandMode.defaultModeUsed != true) {
                                    this.modus = commandMode.modus;
                                }
                                break;
                            }
                            if (input.equalsIgnoreCase(AudioVideo) || input.equalsIgnoreCase(Audio) ||
                                    input.equalsIgnoreCase(Video) || input.equalsIgnoreCase(InteractiveVideo) ||
                                    input.equalsIgnoreCase(LicencedAudio) || input.equalsIgnoreCase(LicencedVideo) ||
                                    input.equalsIgnoreCase(LicencedAudioVideo)) {

                                String mediatype = input;
                                String nameOfProducer = s.next();
                                String tagsInput = s.next();
                                BigDecimal bitrate = BigDecimal.valueOf(Long.parseLong(s.next()));
                                Duration length = Duration.ofMillis(Long.valueOf(s.next()));

                                String optionaleParameter = s.next() + " " + s.next();

                                Collection<Tag> tagCollection = new LinkedList<>();
                                try {
                                    String[] tags = tagsInput.split(",");
                                    for (int i = 0; tags.length > i; i++) {
                                        Tag tag1 = Tag.valueOf(tags[i]);
                                        tagCollection.add(tag1);
                                    }
                                } catch (Exception e) {
                                    System.out.println("No tags set.\n");
                                }
                                if (tagCollection.size() == 0) {
                                    System.out.println("Not tags set.");
                                }

                                MediaEvent event = new MediaEvent(this, mediatype.toLowerCase(), nameOfProducer, tagCollection, bitrate,
                                        length, optionaleParameter);

                                if (null != this.addMediaHandler) {
                                    addMediaHandler.handle(event);
                                }
                                break;
                            } else {
                                ProducerEvent producerEvent = new ProducerEvent(this, input);
                                if (null != this.addProducerHandler) addProducerHandler.handle(producerEvent);
                                System.out.println("Producer added.");
                                break;
                            }

                        case LOESCHMODUS:
                            System.out.println("Please enter command:");
                            input = s.next();
                            if (input.contains(":")) {
                                CommandMode commandMode = new CommandMode(input);
                                //set this mode if default was not used in commandMode
                                if (commandMode.defaultModeUsed != true) {
                                    this.modus = commandMode.modus;
                                }
                                break;
                            }
                            //neue methode hier einfügen um zu wissen, ob producer oder media event
                            // gelöscht werden muss -> rückgabe aus GL, ob producer oder event gefunden
                            //-- gerade wird beides erzeugt
                            DeleteMediaEvent deleteMediaEvent = new DeleteMediaEvent(this, input);
                            ProducerEvent deleteProducerEvent = new ProducerEvent(this, input);

                            if (null != this.deleteMediaEventHandler) {
                                deleteMediaEventHandler.handle(deleteMediaEvent);
                            }
                            if (null != this.deleteProducerHandler) {
                                deleteProducerHandler.handle(deleteProducerEvent);
                            }

                        case ANZEIGEMODUS:
                            System.out.println("enter command: ");
                            input = s.next();
                            if (input.contains(":")) {
                                CommandMode commandMode = new CommandMode(input);
                                //set this mode if default was not used in commandMode
                                if (commandMode.defaultModeUsed != true) {
                                    this.modus = commandMode.modus;
                                }
                                break;
                            }

                            //TODO uploader anzeigen, content typ, oder tags
                            //uploader ist leicht zu realisieren, return der liste anfordern, event mit liste zurückschicken, ausgeben
                            //content same, evtl. noch nach typ filtern und ausgeben
                            //methode schreiben, um verwendete Tags herauszusuchen -> Tags zurückgeben -> arbeitet auch auf Media liste von zuvor

                        case AENDERUNGSMODUS:
                            System.out.println("enter command: ");
                            input = s.next();
                            if (input.contains(":")) {
                                CommandMode commandMode = new CommandMode(input);
                                //set this mode if default was not used in commandMode
                                if (commandMode.defaultModeUsed != true) {
                                    this.modus = commandMode.modus;
                                }
                                break;
                            }
                            ChangeMediaEvent changeMediaEvent = new ChangeMediaEvent(this, input);

                            if (null != this.changeMediaEventHandler) {
                                changeMediaEventHandler.handle(changeMediaEvent);
                            }

                        case PERSISTENZMODUS:
                            //noch nicht bekannt, was das ist
                    }
                }catch (Exception e) {
                    System.out.println("Leider ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut.");
                }
            } while (true);
        }
    }


}
