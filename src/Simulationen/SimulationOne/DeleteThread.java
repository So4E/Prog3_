package Simulationen.SimulationOne;

import administration.Administration;

import java.util.concurrent.ThreadLocalRandom;

public class DeleteThread extends Thread {
    Administration admin;
    int random;

    public DeleteThread(Administration newAdmin) {
        this.admin = newAdmin;

    }

    public void run(){
        do {
            if (admin.listMedia().size() > 0) {
                try {
                    this.random = ThreadLocalRandom.current().nextInt(0,  admin.listMedia().size() +1 );
                    admin.deleteMedia(admin.listMedia().get(random).getAddress());
                    System.out.println(this.getName() + ": " + this.toString() +  ": Tried to delete media at index " + random);
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }while (true);
    }

    @Override
    public String toString (){
        return "DeleteThread";
    }
}
