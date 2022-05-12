package administration;

import mediaDB.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.*;

import static mediaDB.Tag.Tutorial;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AdministrationImplTest {
    Collection<Tag> collection = new LinkedList<>();

    //initializing Administration
    @Test
    void initialiseAdministrationWithSizeSmallerThanZero() {
        assertThrows(InvalidParameterException.class, () -> {
            Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(-1));
        });
    }

    //producer tests ------------------------------------------------------------------------
    @Test
    void addingFirstProducer(){
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        boolean actual = testAdmin.addProducer("JZ");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    void tryToAddProducerTwice(){
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("JZ");

        //try to add same producer (this is case sensitive)
        boolean actual = testAdmin.addProducer("JZ");
        boolean expected = false;

        assertEquals(expected, actual);
    }

    //TODO Test to check if producer is also on Hashmap
    /*@Test
    void checkIfProducerIsAlsoOnMap(){
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("JZ");
        int itemsOnMap = testAdmin.get ->>>>>> getter noch definieren in AdminImpl
        boolean expected = true;
        assertEquals(expected, actual);
    }*/

    //mediafiles tests ---------------------------------------------------------------
    @Test
    void noMediaOnListWithoutAddingAnything() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("jz");

        int actual = testAdmin.listMedia().size();
        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    void addMediaReturnIsCorrect() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("JZ");
        collection.add(Tutorial);
        boolean actual = testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    void addMediaCheckOneItemSavedOnLinkedList() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("JZ");
        collection.add(Tutorial);
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        int actual = testAdmin.listMedia().size();
        int expected = 1;
        assertEquals(expected, actual);
    }

    //to check that no Exception comes back
    @Test
    void listMediaWithNoObjectsOnList() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.listMedia();
    }

    @Test
    void addMediaExceedsMaxSize() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(39));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heino Müller");
        testAdmin.addProducer("Heinz Horst");
        testAdmin.addProducer("Anna Schmidt"); //needs to be added to list, otherwise expected false statement comes from missing producer name

        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heino Müller", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        //storage full -> next media cannot be added:
        boolean actual = testAdmin.addMedia("AudioVideo", "Anna Schmidt", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        boolean expected = false;

        assertEquals(expected, actual);
    }

    @Test
    void changeMedia() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        testAdmin.changeMedia(testAdmin.listMedia().get(0).getAddress());

        long actual = testAdmin.listMedia().get(0).getAccessCount();
        long expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    void increaseAccessCount() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        AudioVideoImpl content = (AudioVideoImpl) testAdmin.listMedia().get(0);
        content.increaseAccessCount();

        long actual = testAdmin.listMedia().get(0).getAccessCount();
        long expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    void increaseAccessCountWithChangeMedia() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        String address = testAdmin.listMedia().get(0).getAddress();
        testAdmin.changeMedia(address);

        long actual = testAdmin.listMedia().get(0).getAccessCount();
        long expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    void deleteFirstMedia() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heinz Horst");

        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "jz", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        testAdmin.deleteMedia(testAdmin.listMedia().getFirst().getAddress());

        int actual = testAdmin.listMedia().size();
        int expected = 3;
        assertEquals(expected, actual);
    }

    void deleteFirstMediaTwo() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heinz Horst");

        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        boolean actual = testAdmin.deleteMedia(testAdmin.listMedia().getLast().getAddress());
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    void deleteLastMedia() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heinz Horst");

        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        //check both return boolean when deleting and size of list
        //check return when deleting an item
        boolean returnWhenDeleted = testAdmin.deleteMedia(testAdmin.listMedia().getLast().getAddress());
        assertTrue(returnWhenDeleted);

        //check that also list is reduced by one item
        int actual = testAdmin.listMedia().size();
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    void badDeleteMediaThatDoesNottExist() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heinz Horst");

        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        String address = testAdmin.listMedia().get(2).getAddress();
        testAdmin.deleteMedia(address);

        boolean actual = testAdmin.deleteMedia(address);
        boolean expected = false;
        assertEquals(expected, actual);
    }
}

    /* -------------------- MOCKITO TUT NICHT -----------> in Übung fragen
    "org.mockito.exceptions.base.MockitoException:
Mockito cannot mock this class: class administration.AdministrationImpl."

    @Test
    void noMediaSavedYet() {
        AdministrationImpl testAdmin = mock(AdministrationImpl.class); -> Antwort: Klasse die ich testen
        möchte kann ich nicht mocken
        int actual = testAdmin.listMedia().size();
        int expected = 0;
        assertEquals(expected, actual);
    }*/