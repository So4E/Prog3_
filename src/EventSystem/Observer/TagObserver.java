package EventSystem.Observer;

import administration.AdministrationImpl;
import mediaDB.Tag;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static mediaDB.Tag.*;

public class TagObserver implements Observer{
    private AdministrationImpl observableAdmin;
    private Map<Tag, Integer> tagMap = new HashMap<>();


    public TagObserver(AdministrationImpl observableAdmin) {
        this.observableAdmin = observableAdmin;
        this.observableAdmin.meldeAn(this);
        this.tagMap = this.observableAdmin.getUseOfTags();
    }

    @Override
    public void aktualisiere() {
        LinkedList<Tag> usedTags = new LinkedList<>();

        int oldAnimal = tagMap.get(Animal);
        int oldTutorial = tagMap.get(Tag.Tutorial);
        int oldLifestyle = tagMap.get(Tag.Lifestyle);
        int oldNews = tagMap.get(Tag.News);

        this.tagMap = this.observableAdmin.getUseOfTags();

        int animal = tagMap.get(Animal);
        int tutorial = tagMap.get(Tag.Tutorial);
        int lifestyle = tagMap.get(Tag.Lifestyle);
        int news = tagMap.get(Tag.News);

        for(Tag c : Tag.values()) {
            if (animal > oldAnimal || tutorial > oldTutorial || lifestyle > oldLifestyle || news > oldNews) {
                System.out.println("Tag Observer: Use of Tag " + c + " has increased since last check.");
            }
            if (animal < oldAnimal || tutorial < oldTutorial || lifestyle < oldLifestyle || news < oldNews) {
                System.out.println("Tag Observer: Use of Tag " + c + " has decreased since last check.");
            }
            if (animal == oldAnimal) {
                System.out.println("Tag Observer: No changes on Tag " + c + " times since last checked.");
            }
        }
    }
}
