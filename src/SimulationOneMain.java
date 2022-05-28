import EventSystem.Observer.Observer;
import EventSystem.Observer.SizeObserver;
import EventSystem.Observer.TagObserver;
import Simulationen.SimObserver.AddOrDeleteObserverImpl;
import Simulationen.SimulationOne.AddThread;
import Simulationen.SimulationOne.DeleteThread;
import administration.AdministrationImpl;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Scanner;

/*
Simulation
Stellen Sie sicher, dass die Geschäftslogik thread-sicher ist. Erstellen Sie dafür Simulationen,
die die Verwendung der Geschäftslogik im Produktivbetrieb testen. Die Abläufe in der Simulation
sollen auf der Konsole dokumentiert werden. Jeder thread muss für jede ändernde Interaktion an der
Geschäftslogik eine Ausgabe produzieren. Jede Änderung an der Geschäftslogik muss eine Ausgabe
produzieren, sinnvollerweise über Beobachter.
Zur Entwicklung darf Thread.sleep o.Ä. verwendet werden, in der Abgabe muss dies deaktiviert sein
bzw. darf nur mit Null-Werten verwendet werden.
Den Simulationen ist die Kapazität per Kommandozeilenargument zu übergeben, wobei 0 ein zulässiger
Wert ist.


Simulation 1
Erstellen Sie einen thread der kontinuierlich versucht eine zufällig erzeugte Mediadatei einzufügen.
Erstellen Sie einen weiteren thread der kontinuierlich die Liste der enthaltenen Mediadateien abruft,
daraus zufällig eine auswählt und löscht. Diese Simulation sollte nicht terminieren und nicht
synchronisiert arbeiten.
 */
public class SimulationOneMain {

    public static void main(String[] args) {
        int capacity = -1;

        //get capacity from user
        System.out.println("Please enter capacity for database: ");
        do {
            Scanner s = new Scanner(System.in);
            String input = s.next();
            try {
                capacity = Integer.parseInt(input);
            } catch (NumberFormatException n) {
                System.out.println("No correct input. Please try again to enter a number bigger or equal to 0.");
            }
        } while (capacity == -1);

        //create admin and prepare for thread test
        AdministrationImpl newAdmin = new AdministrationImpl(BigDecimal.valueOf(capacity));
        newAdmin.addProducer("Otto");
        newAdmin.addProducer("Tina");
        newAdmin.addProducer("Bradley Cooper");
        newAdmin.addProducer("Renate");

        LinkedList<Thread> simulationOneThreads = new LinkedList<>();
        Thread adding = new AddThread(newAdmin);
        Thread deleting = new DeleteThread(newAdmin);
        simulationOneThreads.add(adding);
        simulationOneThreads.add(deleting);

        Observer sizeObserver = new SizeObserver(newAdmin);
        Observer tagObserver = new TagObserver(newAdmin);
        Observer addOrDeleteObserver = new AddOrDeleteObserverImpl(newAdmin);

        deleting.start();
        adding.start();

    }
}
