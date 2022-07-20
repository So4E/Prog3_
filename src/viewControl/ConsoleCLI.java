package viewControl;

import EventSystem.ToGL.*;
import mediaDB.Tag;
import utilities.AnalyzeUserInput;
import utilities.CommandMode;
import utilities.HandlerConfigSuperclassToGLAndBack;
import utilities.Modus;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;
import java.util.Scanner;

import static administration.Mediatype.*;
import static java.lang.System.exit;
import static utilities.Modus.START;

public class ConsoleCLI extends HandlerConfigSuperclassToGLAndBack {
    public Modus modus;
    private AnalyzeUserInput analyzeUserInput = new AnalyzeUserInput();

    public ConsoleCLI() {
        this.modus = START;
    }


    public void execute() {
        printUsage();
        try (Scanner s = new Scanner(System.in)) {
            do {
                try {
                    System.out.println("Please enter command:");
                    //s.next() -> reads until first space, using nextline() would read whole line
                    //until enter or \n
                    String input = s.next().trim();

                    switch (modus) {
                        //START mode is used to set first operation mode; initialized in constructor
                        case START:
                            if (input.charAt(0) == ':') {
                                changeMode(input);
                                break;
                            }
                        case INSERT:
                            //if input starts with : it should be a new mode, so set this mode and move on
                            if (input.charAt(0) == ':') {
                                changeMode(input);
                                break;
                            }
                            if (input.equalsIgnoreCase(audiovideo.toString()) || input.equalsIgnoreCase(audio.toString()) ||
                                    input.equalsIgnoreCase(video.toString()) || input.equalsIgnoreCase(interactivevideo.toString()) ||
                                    input.equalsIgnoreCase(licensedaudio.toString()) || input.equalsIgnoreCase(licensedvideo.toString()) ||
                                    input.equalsIgnoreCase(licensedaudiovideo.toString())) {

                                String mediatype = input.toLowerCase();
                                String nameOfProducer = s.next();
                                String tagsInput = s.next();
                                BigDecimal bitrate = BigDecimal.valueOf(Long.parseLong(s.next()));
                                Duration length = Duration.ofMillis(Long.valueOf(s.next()));
                                String optionaleParameter = " ";
                                try {
                                    optionaleParameter = s.nextLine();
                                } catch (Exception e) {
                                    System.out.println("S.next() for optional parameters did not work");
                                }

                                LinkedList<Tag> tagCollection = analyzeUserInput.splitTagInput(tagsInput);

                                MediaEvent event = new MediaEvent(this, mediatype, nameOfProducer, tagCollection, bitrate,
                                        length, optionaleParameter);

                                if (null != this.addMediaHandler) {
                                    addMediaHandler.handle(event);
                                }
                                break;
                            } else {
                                ProducerEvent producerEvent = new ProducerEvent(this, input, true);
                                if (null != this.addProducerHandler) addProducerHandler.handle(producerEvent);
                                System.out.println("Adding producer.");
                                break;
                            }

                        case DELETE:
                            if (input.charAt(0) == ':') {
                                changeMode(input);
                                break;
                            }
                            //neue methode hier einfügen um zu wissen, ob producer oder media event
                            // gelöscht werden muss -> rückgabe aus GL, ob producer oder event gefunden
                            //-- gerade wird beides erzeugt
                            DeleteMediaEvent deleteMediaEvent = new DeleteMediaEvent(this, input);
                            ProducerEvent deleteProducerEvent = new ProducerEvent(this, input, false);

                            if (null != this.deleteMediaEventHandler) {
                                deleteMediaEventHandler.handle(deleteMediaEvent);
                            }
                            if (null != this.deleteProducerHandler) {
                                deleteProducerHandler.handle(deleteProducerEvent);
                            }

                        case READ:
                            if (input.charAt(0) == ':') {
                                changeMode(input);
                                break;
                            }

                            if (input.equals("content")) {
                                String type = null;
                                /*if(s.hasNext()) {
                                    type = s.next();
                                }*/

                                //ShowMediaListEvent erzeugen und versenden
                                ShowMediaListEvent event = new ShowMediaListEvent(this, type);
                                if (null != this.showMediaListEventHandler) {
                                    showMediaListEventHandler.handle(event);
                                }
                                break;
                            }


                            //TODO uploader anzeigen, content typ, oder tags
                            //uploader ist leicht zu realisieren, return der liste anfordern, event mit liste zurückschicken, ausgeben
                            //content same, evtl. noch nach typ filtern und ausgeben
                            //methode schreiben, um verwendete Tags herauszusuchen -> Tags zurückgeben -> arbeitet auch auf Media liste von zuvor

                        case UPDATE:
                            if (input.charAt(0) == ':') {
                                changeMode(input);
                                break;
                            }
                            ChangeMediaEvent changeMediaEvent = new ChangeMediaEvent(this, input);

                            if (null != this.changeMediaEventHandler) {
                                changeMediaEventHandler.handle(changeMediaEvent);
                            }

                        case PERSIST:
                            if (input.charAt(0) == ':') {
                                changeMode(input);
                                break;
                            }
                            //noch nicht bekannt, was das ist
                            break;

                        case EXIT:
                            System.out.println("Closing CLI.");
                            exit(0);
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("There was a mistake. Please try again.");
                }
            } while (true);
        }
    }

    public void printUsage() {
        System.out.println("\nWELCOME TO MEDIA MANAGEMENT SYSTEM " +
                "\n Please choose operating mode from the following options: \n" +
                ":c  = Switch to insert-mode\n" +
                ":d  = Switch to delete-mode\n" +
                ":r  = Switch to read-mode\n" +
                ":u  = Switch to update-mode\n" +
                ":p  = Switch to persist-mode\n" +
                "..and press enter.\n \n" +
                "To add a MediaFile, please choose specific type from following options: \n" +
                " - audioVideo \n" +
                " - audio \n" +
                " - video \n" +
                " - interactiveVideo \n" +
                " - licencedAudio \n" +
                " - licencedVideo \n" +
                " - licencedAudioVideo \n" +
                "..and combine with additional information, always dividing parameters with one space " +
                "between each parameter: \n" +
                "[Media-type] [uploader] [Tags seperated by comma, lone comma stands for no Tags] [bitrate] " +
                "[length] [[optional parameters]] \n" +
                "\n If you would like to exit the program just type 'exit' and press enter \n ");
    }

    public void changeMode(String input) {
        CommandMode commandMode = new CommandMode(input);
        //set this mode if default was not used in commandMode
        if (!commandMode.defaultModeUsed) {
            this.modus = commandMode.modus;
            System.out.println(this.modus.toString() + "-mode was set");
        } else {
            System.out.println("No valid mode entered. Please try again.");
        }
    }
}
