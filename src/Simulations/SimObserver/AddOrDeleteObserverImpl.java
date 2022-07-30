package Simulations.SimObserver;


import administration.AdministrationImpl;
import administration.AllUploadables;
import Observer_InversionOfControl.Observer;

import java.util.LinkedList;

public class AddOrDeleteObserverImpl implements Observer {
    private AdministrationImpl observableAdmin;
    private LinkedList<AllUploadables> mediaList;

    public AddOrDeleteObserverImpl(AdministrationImpl observableAdmin) {
        this.observableAdmin = observableAdmin;
        this.observableAdmin.meldeAn(this);
        this.mediaList = observableAdmin.getMediaList();
    }

    @Override
    public void aktualisiere() {
        LinkedList<AllUploadables> oldList = this.mediaList;
        LinkedList<AllUploadables> newList = observableAdmin.getMediaList();
        if(oldList.equals(newList)){
            System.out.println("No changes at MediaList made.");
            return;
        }
        else {
            if(oldList.size() < newList.size()) {
                int addedItems = newList.size() - oldList.size();
                System.out.println("+++ Add Observer: Medium has been added to database. " + addedItems +
                        " item/s was/were added to database since last check. Number of items currently saved: " + newList.size());
            }
            if(oldList.size()>newList.size()) {
                int deletedItems = oldList.size() - newList.size() ;
                System.out.println("--- Delete Observer: Medium has been deleted from database. " + deletedItems +
                        " item/s was/were deleted from database since last check. Number of items currently saved: "
                        + newList.size());
            }
        }
        this.mediaList = newList;
    }
}
