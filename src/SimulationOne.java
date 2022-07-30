import observer_InversionOfControl.Observer;
import observer_InversionOfControl.SizeObserver;
import observer_InversionOfControl.TagObserver;
import simulationOne.AddOrDeleteObserverImpl;
import simulationOne.AddThread;
import simulationOne.DeleteThread;
import administration.AdministrationImpl;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.LinkedList;


public class SimulationOne {

    public static void main(String[] args) {
        int capacity = 100;
        int newCapacity = capacity;
        try {
            //try to parse input to an int
            newCapacity = Integer.parseInt(args[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        //any way initialize cli with default or set capacity
        capacity = newCapacity;

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

        PrintStream printStream = System.out;
        Observer sizeObserver = new SizeObserver(newAdmin, printStream);
        Observer tagObserver = new TagObserver(newAdmin);
        Observer addOrDeleteObserver = new AddOrDeleteObserverImpl(newAdmin);

        deleting.start();
        adding.start();

    }
}
