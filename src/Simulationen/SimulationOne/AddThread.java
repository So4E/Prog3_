package Simulationen.SimulationOne;

import administration.Administration;
import mediaDB.Tag;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import static mediaDB.Tag.*;

public class AddThread extends Thread{
    Collection<Tag> tags = new LinkedList<>();
    ArrayList<String> names = new ArrayList<>();

    int randomNumber;
    int listNumber;
    Administration admin;


    public AddThread (Administration administration){
        this.admin = administration;
    }

    public void run() {
        tags.add(Tutorial);
        tags.add(Animal);
        tags.add(Lifestyle);
        tags.add(News);

        names.add("Otto");
        names.add("Tina");
        names.add("Bradley Cooper");
        names.add("Renate");



        do {
            this.randomNumber = ThreadLocalRandom.current().nextInt(2, 5);
            this.listNumber = ThreadLocalRandom.current().nextInt(0, 3);
            admin.addMedia("audiovideo", names.get(listNumber), tags, BigDecimal.valueOf(randomNumber),
                    Duration.ofMinutes(randomNumber), "139 108");
            System.out.println(this.getName() + ": " + this.toString() +": Tried to add new Media");
        }while (true);
    }

    @Override
    public String toString (){
        return "AddThread";
    }
}
