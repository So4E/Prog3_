package observer_InversionOfControl;

import administration.AdministrationImpl;
import mediaDB.Tag;

import java.util.HashMap;
import java.util.Map;

import static mediaDB.Tag.*;

public class TagObserver implements Observer {
    private AdministrationImpl observableAdmin;
    private Map<Tag, Integer> tagMap = new HashMap<>();


    public TagObserver(AdministrationImpl observableAdmin) {
        this.observableAdmin = observableAdmin;
        this.observableAdmin.meldeAn(this);
        this.tagMap = this.observableAdmin.getTagMap();
    }

    @Override
    public void aktualisiere() {
        int oldAnimal = tagMap.get(Animal);
        int oldTutorial = tagMap.get(Tag.Tutorial);
        int oldLifestyle = tagMap.get(Tag.Lifestyle);
        int oldNews = tagMap.get(Tag.News);

        this.tagMap = this.observableAdmin.getTagMap();

        int animal = tagMap.get(Animal);
        int tutorial = tagMap.get(Tag.Tutorial);
        int lifestyle = tagMap.get(Tag.Lifestyle);
        int news = tagMap.get(Tag.News);

        //ideally this sys.out should be outputstream to console, which will print out, change if there's still time
        if (animal > oldAnimal) {
            System.out.println("Tag Observer: Use of Tag " + Animal + " has increased from " + oldAnimal
                    + " to " + animal + ".");
        }
        if (animal < oldAnimal) {
            System.out.println("Tag Observer: Use of Tag " + Animal + " has decreased from " + oldAnimal +
                    " to: " + animal + ".");
        }
        if (tutorial > oldTutorial) {
            System.out.println("Tag Observer: Use of Tag " + Tutorial + " has increased from " + oldTutorial
                    + " to " + tutorial + ".");
        }
        if (tutorial < oldTutorial) {
            System.out.println("Tag Observer: Use of Tag " + Tutorial +  " has decreased from " + oldTutorial +
                    " to: " + tutorial + ".");
        }
        if (lifestyle > oldLifestyle) {
            System.out.println("Tag Observer: Use of Tag " + Lifestyle + " has increased from " + oldLifestyle
                    + " to " + lifestyle + ".");
        }
        if (lifestyle < oldLifestyle) {
            System.out.println("Tag Observer: Use of Tag " + Lifestyle +  " has decreased from " + oldLifestyle +
                    " to: " + lifestyle + ".");
        }
        if (news > oldNews) {
            System.out.println("Tag Observer: Use of Tag " + News + " has increased from " + oldNews
                    + " to " + news + ".");
        }
        if (news < oldNews) {
            System.out.println("Tag Observer: Use of Tag " + News + " has decreased from " + oldNews +
                    " to: " + news + ".");
        }
    }

}
