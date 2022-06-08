package administration;

import mediaDB.AudioVideoImpl;
import mediaDB.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.LinkedList;

import static mediaDB.Tag.Tutorial;
import static org.junit.jupiter.api.Assertions.*;

class AdministrationImplTest {
    LinkedList<Tag> collection = new LinkedList<>();

    //initialization of administration --------------------------------
    @Test
    void initialiseAdministrationWithSizeSmallerThanZero() {
        assertThrows(InvalidParameterException.class, () -> {
            Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(-1));
        });
    }

    //producer tests ------------------------------------------------------------------------
    @Test
    void addingFirstProducer() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        boolean expected = true;
        boolean actual = testAdmin.addProducer("JZ");
        assertEquals(expected, actual);
    }

    @Test
    void tryToAddProducerTwice() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("JZ");

        //try to add same producer (this is case sensitive)
        boolean expected = false;
        boolean actual = testAdmin.addProducer("JZ");

        assertEquals(expected, actual);
    }

    @Test
    void nameSavedCorrectly() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("Horst");

        //check if name is listed in producer list
        boolean expected = true;
        boolean actual = testAdmin.checkIfProducerIsListed("Horst");

        assertEquals(expected, actual);
    }

    /*@Test ----> obsolet, da MediaCount nun in Uploader gespeichert
    void checkIfProducerIsAlsoOnMapForCounts() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("Horst");
        int expected = 0;
        int actualMediaCount = testAdmin.listProducerWithMediaCount().get("Horst");
        assertEquals(expected, actualMediaCount);
    }*/

    @Test
    void checkIfProducerCountsAreAdjustedWhenMediaFilesAreBeingAddedAndDeleted() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("Horst");

        testAdmin.addMedia("AudioVideo", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        testAdmin.deleteMedia(testAdmin.listMedia().get(1).getAddress());
        testAdmin.addMedia("Audio", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        int horstsIndex = testAdmin.getIndexOfProducer("Horst");

        int expected = 3;
        int actualMediaCount = testAdmin.listSuperProducer().get(horstsIndex).getMediaCount();
        assertEquals(expected, actualMediaCount);
    }

    @Test
    void checkWhetherReturnOfSearchForDeletedProducerIsCorrect() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("Horst");
        testAdmin.addProducer("Hildegard");
        testAdmin.addProducer("BABA");

        testAdmin.deleteProducer("Horst");
        int expected = -1;
        int indexDefaultReturn = testAdmin.getIndexOfProducer("Horst");
        assertEquals(expected, indexDefaultReturn);
    }

    @Test
    void checkWhetherReturnOfCounterMapSearchForDeletedProducerIsCorrect() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("Horst");
        testAdmin.addProducer("Hildegard");
        testAdmin.addProducer("BABA");

        testAdmin.addMedia("AudioVideo", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        testAdmin.deleteMedia(testAdmin.listMedia().get(1).getAddress());

        testAdmin.deleteProducer("Horst");

        int horstsIndex = testAdmin.getIndexOfProducer("Horst");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            testAdmin.listProducer().get(horstsIndex);
        });
    }

    @Test
    void tryToDeleteProducerThatDoesntExist() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));

        boolean expected = false;
        boolean actual = testAdmin.deleteProducer("Horst");

        assertEquals(expected, actual);
    }

    //mediafiles tests ---------------------------------------------------------------
    @Test
    void nothingOnMediaListWithoutAddingAnything() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("jz");

        int expected = 0;
        int actual = testAdmin.listMedia().size();
        assertEquals(expected, actual);
    }

    @Test
    void addMediaReturnIsCorrect() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("JZ");
        collection.add(Tutorial);
        boolean expected = true;
        boolean actual = testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        assertEquals(expected, actual);
    }

    @Test
    void addMediaCheckOneItemSavedOnLinkedList() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("JZ");
        collection.add(Tutorial);
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        int expected = 1;
        int actual = testAdmin.listMedia().size();
        assertEquals(expected, actual);
    }

    @Test
    void listMediaWithNoObjectsOnList() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        int expected = 0;
        int actualSize = testAdmin.listMedia().size();
        assertEquals(expected, actualSize);
    }

    @Test
    void getMediaWithNoObjectsOnMediaList() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            testAdmin.listMedia().get(3);
        });
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
        boolean expected = false;
        boolean actual = testAdmin.addMedia("AudioVideo", "Anna Schmidt", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

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

        long expected = 1;
        long actual = testAdmin.listMedia().get(0).getAccessCount();
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

        long expected = 1;
        long actual = testAdmin.listMedia().get(0).getAccessCount();
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

        long expected = 1;
        long actual = testAdmin.listMedia().get(0).getAccessCount();
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

        int expected = 3;
        int actual = testAdmin.listMedia().size();
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

        boolean expected = true;
        boolean actual = testAdmin.deleteMedia(testAdmin.listMedia().getLast().getAddress());
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
        int expected = 3;
        int actual = testAdmin.listMedia().size();
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

        boolean expected = false;
        boolean actual = testAdmin.deleteMedia(address);
        assertEquals(expected, actual);
    }


    @Test
    void getMediaFromSpecialMediaType() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heinz Horst");

        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("Audio", "jz", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("LicensedAudio", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("InteractiveVideo", "Ursula", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        int expected = 2;
        int actualHowManyAudioVideo = testAdmin.listMedia("AuDIoVideo").size();

        assertEquals(expected,actualHowManyAudioVideo);
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