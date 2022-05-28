package utilities;

public class CommandMode {
    public Modus modus;
    public String commandString;
    public boolean defaultModeUsed;

    public CommandMode(String text) {
        //reset commandString and default boolean
        commandString = null;
        boolean defaultModeUsed = false;
        String input = text;
        try {
            input = text.substring(0, 2);
        } catch (Exception iputTooSmall) {
            input = "default";
        }
        //
        switch (input) {
            case ":c":
                this.modus = Modus.INSERT;
                break;
            case ":d":
                this.modus = Modus.DELETE;
                break;
            case ":r":
                this.modus = Modus.READ;
                break;
            case ":u":
                this.modus = Modus.UPDATE;
                break;
            case ":p":
                this.modus = Modus.PERSIST;
                break;
            default:
                this.defaultModeUsed = true;
                break;
        }
    }
}
