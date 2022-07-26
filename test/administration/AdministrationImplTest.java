package administration;

import EventSystem.Observer_InversionOfControl.Observer;
import EventSystem.Observer_InversionOfControl.SizeObserver;
import mediaDB.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.UUID;

import static mediaDB.Tag.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void checkIfProducerCountsAreAdjustedWhenMediaFilesAreBeingAddedAndDeleted() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("Horst");

        testAdmin.addMedia("InteractiveVideo", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("Video", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "");
        testAdmin.deleteMedia(testAdmin.getMediaList().get(1).getAddress());
        testAdmin.addMedia("Audio", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("LicensedVideo", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
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

        testAdmin.addMedia("Video", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        testAdmin.deleteMedia(testAdmin.getMediaList().get(1).getAddress());

        testAdmin.deleteProducer("Horst");

        int horstsIndex = testAdmin.getIndexOfProducer("Horst");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            testAdmin.getProducerList().get(horstsIndex);
        });
    }

    @Test
    void checkIfMediaFilesBeenDeletedWhenDeletingProducer() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("Horst");
        testAdmin.addProducer("Hildegard");
        testAdmin.addProducer("BABA");

        testAdmin.addMedia("AudioVideo", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Horst", collection, BigDecimal.valueOf(10), Duration.ofMinutes(5), "139 108");

        testAdmin.deleteProducer("Horst");

        int expected = 0;
        int actual = testAdmin.getMediaList().size();

        assertEquals(expected, actual);
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
        int actual = testAdmin.getMediaList().size();
        assertEquals(expected, actual);
    }

    @Test
    void addMediaReturnIsCorrect() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("JZ");
        collection.add(Tutorial);
        boolean expected = true;
        boolean actual = testAdmin.addMedia("LicensedAudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "WarnerBros 139 108");
        assertEquals(expected, actual);
    }

    @Test
    void addLicencedAudioVideoWithIncorrectParametersSetsDefaults() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("JZ");
        collection.add(Tutorial);
        boolean expected = true;
        boolean actual = testAdmin.addMedia("LicensedAudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "345 Werner ooops");
        assertEquals(expected, actual);
    }

    @Test
    void addMediaCheckOneItemSavedOnLinkedList() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer("JZ");
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "");

        int expected = 1;
        int actual = testAdmin.getMediaList().size();
        assertEquals(expected, actual);
    }

    @Test
    void listMediaWithNoObjectsOnList() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        int expected = 0;
        int actualSize = testAdmin.getMediaList().size();
        assertEquals(expected, actualSize);
    }

    @Test
    void getMediaWithNoObjectsOnMediaList() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            testAdmin.getMediaList().get(3);
        });
    }

    @Test
    void getTotalSize() {
        AdministrationImpl testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heinz Horst");

        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("Audio", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("LicensedAudio", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("Video", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 Dieter");

        BigDecimal expected = BigDecimal.valueOf(4 * 10);
        BigDecimal actualHowManyTutorialTagsOnTagList = testAdmin.getTotalSize();

        assertEquals(expected, actualHowManyTutorialTagsOnTagList);
    }

    @Test
    void getMaxSizeOfMemory() {
        AdministrationImpl testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));

        BigDecimal expected = BigDecimal.valueOf(100);
        BigDecimal actualHowManyTutorialTagsOnTagList = testAdmin.getMaxSizeOfMemory();

        assertEquals(expected, actualHowManyTutorialTagsOnTagList);
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

        testAdmin.changeMedia(testAdmin.getMediaList().get(0).getAddress());

        long expected = 1;
        long actual = testAdmin.getMediaList().get(0).getAccessCount();
        assertEquals(expected, actual);
    }

    @Test
    void increaseAccessCount() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        AudioVideoImpl content = (AudioVideoImpl) testAdmin.getMediaList().get(0);
        content.increaseAccessCount();

        long expected = 1;
        long actual = testAdmin.getMediaList().get(0).getAccessCount();
        assertEquals(expected, actual);
    }

    @Test
    void increaseAccessCountWithChangeMedia() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        String address = testAdmin.getMediaList().get(0).getAddress();
        testAdmin.changeMedia(address);

        long expected = 1;
        long actual = testAdmin.getMediaList().get(0).getAccessCount();
        assertEquals(expected, actual);
    }

    @Test
    void tryChangeMediaWithNonExistantAddress() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        UUID random = UUID.randomUUID();

        boolean expected = false;
        boolean actual = testAdmin.changeMedia(random.toString());
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

        testAdmin.deleteMedia(testAdmin.getMediaList().getFirst().getAddress());

        int expected = 3;
        int actual = testAdmin.getMediaList().size();
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
        boolean actual = testAdmin.deleteMedia(testAdmin.getMediaList().getLast().getAddress());
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
        boolean returnWhenDeleted = testAdmin.deleteMedia(testAdmin.getMediaList().getLast().getAddress());
        assertTrue(returnWhenDeleted);

        //check that also list is reduced by one item
        int expected = 3;
        int actual = testAdmin.getMediaList().size();
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
        String address = testAdmin.getMediaList().get(2).getAddress();
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
        int actualHowManyAudioVideo = testAdmin.getMediaList("AuDIoVideo").size();

        assertEquals(expected, actualHowManyAudioVideo);
    }

    @Test
    void getUseOfTagsTutorial() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heinz Horst");

        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("Audio", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("LicensedAudio", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        //Ursula is not saved as producer -> media file will not be added
        testAdmin.addMedia("InteractiveVideo", "Ursula", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        int expected = 4;
        int actualHowManyTutorialTagsOnTagList = testAdmin.getTagMap().get(Tutorial);

        assertEquals(expected, actualHowManyTutorialTagsOnTagList);
    }

    @Test
    void getUseOfTagsNews() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        collection.add(Tutorial);

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heinz Horst");

        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("Audio", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        collection.add(News);
        testAdmin.addMedia("LicensedAudio", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        //Ursula is not saved as producer -> media file will not be added
        testAdmin.addMedia("InteractiveVideo", "Ursula", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        int expected = 2;
        int actualHowManyTutorialTagsOnTagList = testAdmin.getTagMap().get(News);

        assertEquals(expected, actualHowManyTutorialTagsOnTagList);
    }

    @Test
    void getUsedTags() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heinz Horst");

        collection.add(Tutorial);
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("Audio", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        collection.add(News);
        testAdmin.addMedia("LicensedAudio", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        LinkedList<Tag> expectedTags = new LinkedList<>();
        expectedTags.add(Tutorial);
        expectedTags.add(News);

        LinkedList<Tag> actuallyReturnedUsedTags = testAdmin.getUsedTags();

        assertEquals(expectedTags, actuallyReturnedUsedTags);
    }

    @Test
    void getNotUsedTags() {
        Administration testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));

        testAdmin.addProducer("JZ");
        testAdmin.addProducer("Heinz Horst");

        collection.add(Tutorial);
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("Audio", "JZ", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        collection.add(News);
        testAdmin.addMedia("LicensedAudio", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");
        testAdmin.addMedia("AudioVideo", "Heinz Horst", collection, BigDecimal.valueOf(10),
                Duration.ofMinutes(5), "139 108");

        LinkedList<Tag> expectedTags = new LinkedList<>();
        expectedTags.add(Animal);
        expectedTags.add(Lifestyle);

        LinkedList<Tag> actuallyReturnedNotUsedTags = testAdmin.getNotUsedTags();

        assertEquals(expectedTags, actuallyReturnedNotUsedTags);
    }

    /////////////////////////////////////////////////////////////////////////////
    //---------------------MOCKITO TESTS ----------------------------

   @Test
    void addMockedMediaFileGood(){
        UploaderImpl mockProducer = mock(UploaderImpl.class);
        when(mockProducer.getName()).thenReturn("Anton");

        AdministrationImpl testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));

        boolean expected = true;
        boolean actual = testAdmin.addProducer(mockProducer.getName());
        assertEquals(expected, actual);
    }

    @Test
    void whenFileAddedCheckedWhetherProducerWithThisNameWasAlreadySavedBefore(){
        UploaderImpl mockProducer = mock(UploaderImpl.class);
        when(mockProducer.getName()).thenReturn("Anton");

        AdministrationImpl testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        testAdmin.addProducer(mockProducer.getName());
        testAdmin.addMedia("AudioVideo", "Anton", collection, BigDecimal.valueOf(50),
                Duration.ofMinutes(5), "139 108");

        verify(mockProducer,times(1)).getName();
        verify(mockProducer,never()).getMediaCount();
        verifyNoMoreInteractions(mockProducer);
        assertTrue(mockProducer.mediaCount == 0);
    }

    @Test
    void meldeAnObserver() {
        AdministrationImpl testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        Observer testObserver = mock(SizeObserver.class);
        testAdmin.meldeAn(testObserver);

        testAdmin.addProducer("JZ");
        collection.add(Tutorial);
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(50),
                Duration.ofMinutes(5), "139 108");
        verify(testObserver).aktualisiere();
    }

    @Test
    void meldeAb() {
        AdministrationImpl testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        Observer testObserver = mock(SizeObserver.class);
        testAdmin.meldeAn(testObserver);

        testAdmin.addProducer("JZ");
        collection.add(Tutorial);
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(50),
                Duration.ofMinutes(5), "139 108");
        verify(testObserver).aktualisiere();

        testAdmin.meldeAb(testObserver);
        testAdmin.addMedia("AudioVideo", "JZ", collection, BigDecimal.valueOf(50),
                Duration.ofMinutes(5), "139 108");
        verifyNoMoreInteractions(testObserver);
    }

    @Test
    void benachrichtige() {
        AdministrationImpl testAdmin = new AdministrationImpl(BigDecimal.valueOf(100));
        Observer testObserver = mock(SizeObserver.class);
        testAdmin.meldeAn(testObserver);

        testAdmin.benachrichtige();
        verify(testObserver).aktualisiere();
    }

}