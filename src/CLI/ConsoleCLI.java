package CLI;

import administration.Mediatype;
import eventSystem.toGL.aendernModus.ChangeMediaEvent;
import eventSystem.toGL.anzeigenmodus.ShowMediaListEvent;
import eventSystem.toGL.anzeigenmodus.ShowProducerListEvent;
import eventSystem.toGL.anzeigenmodus.ShowTagsEvent;
import eventSystem.toGL.einfuegenUndLoeschenModus.DeleteMediaEvent;
import eventSystem.toGL.einfuegenUndLoeschenModus.MediaEvent;
import eventSystem.toGL.einfuegenUndLoeschenModus.ProducerEvent;
import eventSystem.toGL.persistenzmodus.SaveWithJOSEvent;
import mediaDB.Tag;
import utilities.AnalyzeUserInput;
import utilities.CommandMode;
import utilities.HandlerConfigSuperclassToGLAndBack;
import utilities.Modus;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Scanner;

import static administration.Mediatype.*;
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
                    System.out.println("\n-- Please enter command:");
                    //s.next() -> reads until first space, using nextline() would read whole line
                    //until enter or \n
                    String[] completeInput = s.nextLine().trim().split(" ");
                    String input = completeInput[0];

                    switch (modus) {
                        //START mode is used to set first operation mode; initialized in constructor
                        case START:
                            try {
                                if (input.charAt(0) == ':') {
                                    changeMode(input);
                                    break;
                                }
                            } catch (StringIndexOutOfBoundsException indexOutOfBoundsException) {
                                System.out.println("No valid input. Please try again.");
                                break;
                            }
                            System.out.println("Please enter mode first.");
                            break;

                        case INSERT:
                            //if input starts with : it should be a new mode, so set this mode and move on
                            try {
                                if (input.charAt(0) == ':') {
                                    changeMode(input);
                                    break;
                                }
                            } catch (StringIndexOutOfBoundsException indexOutOfBoundsException) {
                                System.out.println("No valid input. Please try again.");
                                break;
                            }
                            if (input.equalsIgnoreCase(audiovideo.toString()) || input.equalsIgnoreCase(audio.toString()) ||
                                    input.equalsIgnoreCase(video.toString()) || input.equalsIgnoreCase(interactivevideo.toString()) ||
                                    input.equalsIgnoreCase(licensedaudio.toString()) || input.equalsIgnoreCase(licensedvideo.toString()) ||
                                    input.equalsIgnoreCase(licensedaudiovideo.toString())) {

                                //create essential parameters
                                String mediatype = input.toLowerCase();
                                String nameOfProducer = "";
                                String tagsInput = "";
                                BigDecimal bitrate = new BigDecimal(0);
                                Duration length = Duration.ofMillis(0);

                                //try get them from input
                                try {
                                    nameOfProducer = completeInput[1];
                                    tagsInput = completeInput[2];
                                    bitrate = BigDecimal.valueOf(Long.parseLong(completeInput[3]));
                                    length = Duration.ofMillis(Long.valueOf(completeInput[4]));
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    //case input had mistakes or missing parameters
                                    System.out.println("There are essential parameters missing, please check your input.");
                                    break;
                                }

                                //due to bad design decisions in the beginning, optionale parameter here needs to be assembled to string again
                                StringBuilder optionaleParameterArray = new StringBuilder();
                                String optionaleParameter;
                                for (int i = 5; i < completeInput.length; i++) {
                                    optionaleParameterArray.append(completeInput[i]);
                                    optionaleParameterArray.append(" ");
                                }
                                optionaleParameter = optionaleParameterArray.toString();

                                if (optionaleParameter.isEmpty()) {
                                    System.out.println("No optional parameters - default was set");
                                    optionaleParameter = " ";
                                }

                                LinkedList<Tag> tagCollection = analyzeUserInput.splitTagInput(tagsInput);

                                MediaEvent event = new MediaEvent(this, mediatype, nameOfProducer, tagCollection, bitrate,
                                        length, optionaleParameter);

                                if (null != this.addMediaHandler) {
                                    addMediaHandler.handle(event);
                                }
                                System.out.println("Adding " + mediatype);
                                break;
                            } else {
                                ProducerEvent producerEvent = new ProducerEvent(this, input, true);
                                if (null != this.addProducerHandler) {
                                    System.out.println("Adding " + input + " as Producer to Media DB.");
                                    addProducerHandler.handle(producerEvent);
                                }
                                break;
                            }

                        case DELETE:
                            try {
                                if (input.charAt(0) == ':') {
                                    changeMode(input);
                                    break;
                                }
                            } catch (StringIndexOutOfBoundsException indexOutOfBoundsException) {
                                System.out.println("No valid input. Please try again.");
                                break;
                            }
                            //not important to know whether user or mediafile should be deleted
                            // only one option can work -> so:
                            DeleteMediaEvent deleteMediaEvent = new DeleteMediaEvent(this, input);
                            ProducerEvent deleteProducerEvent = new ProducerEvent(this, input, false);

                            if (null != this.deleteMediaEventHandler) {
                                System.out.println("Searching whether MediaFile : " + input + " exists. If found, will be deleted.");
                                deleteMediaEventHandler.handle(deleteMediaEvent);
                            }
                            if (null != this.deleteProducerHandler) {
                                System.out.println("Searching whether Producer : " + input + " exists. If found, will be deleted.");
                                deleteProducerHandler.handle(deleteProducerEvent);
                            }
                            break;

                        case READ:
                            try {
                                if (input.charAt(0) == ':') {
                                    changeMode(input);
                                    break;
                                }
                            } catch (StringIndexOutOfBoundsException indexOutOfBoundsException) {
                                System.out.println("No valid input. Please try again.");
                                break;
                            }

                            if (input.equals("content")) {
                                String potentialType = "";
                                String nonsenseInput = "";
                                try {
                                    potentialType = completeInput[1];
                                    for (Mediatype c : Mediatype.values()) {
                                        if (potentialType.equals(c)) {
                                            potentialType = c.toString();
                                        }
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    //no type input there, no need to do anything
                                }

                                //ShowMediaListEvent erzeugen und versenden
                                ShowMediaListEvent event = new ShowMediaListEvent(this, potentialType);
                                if (null != this.showMediaListEventHandler) {
                                    System.out.println("Loading files from Media DB....");
                                    showMediaListEventHandler.handle(event);
                                }
                                break;
                            }

                            if (input.equals("uploader")) {
                                ShowProducerListEvent event = new ShowProducerListEvent(this);
                                if (null != this.showProducerListEventHandler) {
                                    System.out.println("Loading uploaders from Media DB...");
                                    showProducerListEventHandler.handle(event);
                                }
                                break;
                            }

                            if (input.equals("tag") && null != this.showTagsEventHandler) {
                                boolean rightIdentifierSet = false;
                                boolean whichTagsDoYouWant = false;
                                try {
                                    if (completeInput[1].equalsIgnoreCase("i")) {
                                        whichTagsDoYouWant = true;
                                        rightIdentifierSet = true;
                                    } else if (completeInput[1].equalsIgnoreCase("e")) {
                                        whichTagsDoYouWant = false;
                                        rightIdentifierSet = true;
                                    } else {
                                        System.out.println("Specifier must be (i) for used or (e) for unused Tags. Please try again.");
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    System.out.println("Please specify in your tag request, whether you want to see used (i) or unused (e) Tags.");
                                }

                                if (rightIdentifierSet) {
                                    ShowTagsEvent tagsEvent = new ShowTagsEvent(this, whichTagsDoYouWant);
                                    if (null != this.showTagsEventHandler) {
                                        System.out.println("Loading Tags from Medie DB...");
                                        showTagsEventHandler.handle(tagsEvent);
                                    }
                                    break;
                                }
                            } else {
                                System.out.println("No valid input. Please try again.");
                            }
                            break;

                        case UPDATE:
                            try {
                                if (input.charAt(0) == ':') {
                                    changeMode(input);
                                    break;
                                }
                            } catch (StringIndexOutOfBoundsException indexOutOfBoundsException) {
                                System.out.println("No valid input. Please try again.");
                                break;
                            }
                            System.out.println("Updating " + input);
                            ChangeMediaEvent changeMediaEvent = new ChangeMediaEvent(this, input);
                            if (null != this.changeMediaEventHandler) {
                                changeMediaEventHandler.handle(changeMediaEvent);
                            }
                            break;

                        case PERSIST:
                            try {
                                if (input.charAt(0) == ':') {
                                    changeMode(input);
                                    break;
                                }
                            } catch (StringIndexOutOfBoundsException indexOutOfBoundsException) {
                                System.out.println("No valid input. Please try again.");
                                break;
                            }

                            if (input.equals("saveJOS")) {
                                SaveWithJOSEvent saveJOS = new SaveWithJOSEvent(this);
                                if (null != this.saveWithJOSEventHandler) {
                                    saveWithJOSEventHandler.handle(saveJOS);
                                    System.out.println("Saving with JOS.");
                                }
                                break;
                            }

                            if (input.equals("saveJBP")) {
                                System.out.println("Service currently not available.");
                                break;
                            }

                            if (input.equals("loadJOS")) {
                                System.out.println("Service currently not available.");
                                break;
                            }

                            if (input.equals("loadJBP")) {
                                System.out.println("Service currently not available.");
                                break;
                            } else {
                                System.out.println("No valid input. Please try again.");
                                break;
                            }
                    }
                } catch (MissingResourceException missingResourceException) {
                    System.out.println("Warning. The language you chose has not been fully implemented." +
                            "\n This might cause problems with using the database." +
                            "\n Please restart CLI with parameter [DE] to have full access to logging.");
                } catch (Exception e) {
                    e.printStackTrace();
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
                "\n If you would like to exit the program just type ':x' and press enter \n ");
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

    public void print(String string) {
        System.out.println(string);
    }
}
