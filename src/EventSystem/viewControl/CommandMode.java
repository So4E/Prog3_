package EventSystem.viewControl;

import EventSystem.EventLogicToGL.ProducerEventListener;

public class CommandMode {
    public ProducerEventListener.Modus modus;
    public String commandString;
    boolean defaultModeUsed;

    public CommandMode(String text){
        //reset commandString and default boolean
        commandString = null;
        boolean defaultModeUsed = false;
        String input = text;

        try { input=text.substring(0,2);} catch (Exception d){};
        //check if this is a mode command with ":" at the beginning
        if(input.charAt(0) == ':') {
            //if so, set chosen mode
            switch (input) {
                case ":c":
                    this.modus = ProducerEventListener.Modus.EINFUEGEMODUS;
                    System.out.println("Einfügemodus gesetzt.");
                    break;
                case ":d":
                    this.modus = ProducerEventListener.Modus.LOESCHMODUS;
                    System.out.println("Löschmodus gesetzt.");
                    break;
                case ":r":
                    this.modus = ProducerEventListener.Modus.ANZEIGEMODUS;
                    System.out.println("Anzeigemodus gesetzt.");
                    break;
                case ":u":
                    this.modus = ProducerEventListener.Modus.AENDERUNGSMODUS;
                    System.out.println("Änderungsmodus gesetzt.");
                    break;
                case ":p":
                    this.modus = ProducerEventListener.Modus.PERSISTENZMODUS;
                    System.out.println("Persistenzmodus gesetzt.");
                    break;
                default:
                    System.out.println("No valid mode entered. Please try again.");
                    this.defaultModeUsed = true;
                    break;
            }
        }
        //if not save inputString for Console to execute input Command
        else { commandString = text;}
    }
}
