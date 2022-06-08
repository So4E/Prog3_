package administration;

import EventSystem.Observer.Observer;
import EventSystem.Observer.SubjectForSizeObserver;
import mediaDB.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.*;

import static mediaDB.Tag.Animal;


public class AdministrationImpl implements Administration, SubjectForSizeObserver, Serializable {
    //member for MediaMemory
    private LinkedList<AllUploadables> mediaList = new LinkedList<AllUploadables>();
    private BigDecimal maxSizeOfMemory = BigDecimal.valueOf(0);
    private BigDecimal totalSize= BigDecimal.valueOf(0);

    //TODO wo checke ich die Version der Serialisation? bei alternativem Constructor für JBP
    static final long serialVersionUID = 1L;

    //list of producers (case sensitive)
    private LinkedList<UploaderSuper> producerList = new LinkedList<UploaderSuper>();
    private Map<Tag, Integer> tagMap = new HashMap<Tag, Integer>();

    //Todo falls ganze admin persistierbar sein soll -> mit transient objecte markieren, die nicht serialisiert werden sollen
    //observer
    private transient List<Observer> beobachterList;

    //constructor
    public AdministrationImpl(BigDecimal capacity) {
        //check if input BigDecimal is of a required minimum size for the memory, otherwise throw exception
        BigDecimal minimumSize = new BigDecimal(0);
        if (capacity.compareTo(minimumSize) >= 0) {
            this.maxSizeOfMemory = capacity;
            //if input size is too small throw exception
        } else throw new InvalidParameterException();

        tagMap.put(Animal, 0);
        tagMap.put(Tag.Tutorial, 0);
        tagMap.put(Tag.Lifestyle, 0);
        tagMap.put(Tag.News, 0);
    }

    //default constructor for JBP
    public AdministrationImpl(BigDecimal capacity, LinkedList<UploaderSuper> uploaderlist, HashMap<Tag, Integer> tagMap,
                              LinkedList<AllUploadables> mediaList, BigDecimal totalSize) {
        this.maxSizeOfMemory = capacity;
        this.producerList = uploaderlist;
        this.tagMap = tagMap;
        this.mediaList = mediaList;
        this.totalSize = totalSize;
    }



    //--------------------Producer Methods ---------------------------------------------------
    @Override
    public synchronized boolean addProducer(String name) {

        //check if producer is already in list - return false if listed
        if (checkIfProducerIsListed(name) == true) {
            return false;
        }

        //if producer is not in list add him to list and map
        UploaderSuper newProducer = new UploaderImpl(name);
        producerList.add(newProducer);
        newProducer.mediaCount = 0;
        return true;
    }

    @Override
    public synchronized LinkedList<Uploader> listProducer() {
        return new LinkedList<>(this.producerList);
    }

    @Override
    public synchronized LinkedList<UploaderSuper> listSuperProducer() {
        return new LinkedList<>(this.producerList);
    }

    /*@Override
    public synchronized Map<String, Integer> listProducerWithMediaCount() {
        return new HashMap<String, Integer>(this.uploaderMap);
    }*/

    @Override
    public synchronized boolean deleteProducer(String name) {
        //check if producer is in list - return false if not listed
        if (checkIfProducerIsListed(name) == false) {
            return false;
        }
        //if listed - delete producer without deleting associated media objects
        else {
            producerList.remove(getIndexOfProducer(name));
            //uploaderMap.remove(name);
        }
        return true;
    }

    //falls es nicht läuft Änderungen bis hier wegnehmen
    public synchronized boolean checkIfProducerIsListed(String name) {
        for (int i = 0; i < producerList.size(); i++) {
            String checkName = producerList.get(i).getName();
            //if producer is in list return true;
            if (checkName.equals(name)) {
                return true;
            }
        }
        //else return false
        return false;
    }

    public synchronized int getIndexOfProducer(String name) {
        int indexOfProducer = -1;
        for (int i = 0; i < producerList.size(); i++) {
            String checkName = producerList.get(i).getName();
            if (checkName.equals(name)) {
                indexOfProducer = i;
                break;
            }
        }
        return indexOfProducer;
    }


    //------------------- Media Methods----------------------------------------------------

    @Override
    public synchronized boolean addMedia(String mediaType, String nameOfProducer, Collection<Tag> tags, BigDecimal bitrate,
                                         Duration length, String optionaleParameter) {

        //check if nameOfProducer is not yet in producer list: return false if producer needs to be added first
        if (!checkIfProducerIsListed(nameOfProducer)) {
            return false;
        }
        // if producer was already saved, remember for upload of media files
        UploaderSuper producer = producerList.get(getIndexOfProducer(nameOfProducer));

        //calculate size of Media Object (random formula, in case this needs to be correct, change
        long calculateSize = bitrate.longValue() + length.toHours(); // size will be only bitrate with this formula
        BigDecimal size = new BigDecimal(calculateSize);


        //--------- check if maxSize is extended when adding new item
        // -> if so return false, if not, add size of item to total size of savedItems
        BigDecimal newSize = totalSize.add(size);
        if (newSize.compareTo(maxSizeOfMemory) > 0) {
            return false; //TO DO hier evtl. besser Exception nutzen für storage voll?
        }

        UUID mediaID = UUID.randomUUID();
        String address = mediaID.toString();
        AllUploadables newItem;
        Date uploadDate = new Date(); // Date-Object of this exact day and time

        switch (Mediatype.valueOf(mediaType.toLowerCase())) {
            case audiovideo:
                newItem = new AudioVideoImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, size, address, uploadDate);
                break;
            case audio:
                newItem = new AudioImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, size, address, uploadDate);
                break;
            case video:
                newItem = new VideoImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, size, address, uploadDate);
                break;
            case interactivevideo:
                newItem = new InteractiveVideoImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, size, address, uploadDate);
                break;
            case licensedaudio:
                newItem = new LicensedAudioImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, size, address, uploadDate);
                break;
            case licensedvideo:
                newItem = new LicensedVideoImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, size, address, uploadDate);
                break;
            case licensedaudiovideo:
                newItem = new LicensedAudioVideoImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, size, address, uploadDate);
                break;
            default:
                return false;
        }

        mediaList.add(newItem);                  //add new item
        totalSize = totalSize.add(size);         //add its size to total size
        //add plus one in tagMap for used Tags
        if (!tags.isEmpty()) {
            for (Tag c : Tag.values()) {
                int actual = tagMap.get(c);
                if (actual >= 0) {
                    tagMap.put(c, actual + 1);
                }
            }
        }

        benachrichtige();                       //tell SizeObserver that there were changes made
        UploaderSuper superProducer = producer;
        superProducer.increaseMediaCount();
        //int oldCounter = uploaderMap.get(nameOfProducer); // get producer counter
        //uploaderMap.put(nameOfProducer, oldCounter + 1); // increase and save

        return true;
    }

    @Override
    public synchronized LinkedList<Content> listMedia() {
        return new LinkedList<>(this.mediaList);
    }

    @Override
    public synchronized LinkedList<Content> listMedia(String type) {
        LinkedList<Content> theseMediaList = new LinkedList<>();
        for (int i = 0; i < mediaList.size(); i++) {
            if (mediaList.get(i).toString().equals(type.toLowerCase())) {
                theseMediaList.add(mediaList.get(i));
            }
        }
        return theseMediaList;
    }

    @Override
    public synchronized boolean changeMedia(String address) {
        int index = -1;

        //check if there is a media file with this unique address
        for (int i = 0; i < mediaList.size(); i++) {
            //if it contains address increaseAccessCount
            if (mediaList.get(i).getAddress().equals(address)) {
                index = i;
                mediaList.get(index).increaseAccessCount();
                benachrichtige();
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean deleteMedia(String address) {

        //find object with input address and delete it
        for (int i = 0; i < mediaList.size(); i++) {
            Content object = mediaList.get(i);
            if (object.getAddress().equals(address)) {           //object to be deletedfound
                //---- get Uploader of this object
                Uploadable thisObject = (Uploadable) object;
                String uploaderName = thisObject.getUploader().getName();
                //---- and decrease MediaCounter for this uploader -1 (put overwrites existing int at same key)
                //int oldMediaCounter = uploaderMap.get(uploaderName);
                //uploaderMap.put(uploaderName, oldMediaCounter - 1);
                int index = getIndexOfProducer(uploaderName);
                if (index >= 0) {
                    producerList.get(index).decreaseMediaCount();
                }


                //get size of object and decrease used storage space
                MediaContent thatObject = (MediaContent) object;
                BigDecimal size = thatObject.getSize();
                BigDecimal oldSize = totalSize;
                totalSize = new BigDecimal(oldSize.doubleValue() - size.doubleValue());

                //count minus one in Map of used Tags for all Tags of this mediaFile
                Collection<Tag> tags = thatObject.getTags();
                if (!tags.isEmpty()) {
                    for (int a = 0; a < tags.size(); a++) {
                        for (Tag c : Tag.values()) {
                            int actual = tagMap.get(c);
                            if (actual > 0) {
                                tagMap.put(c, actual - 1);
                            }
                        }
                    }
                }

                //delete item
                mediaList.remove(i);
                benachrichtige();
                return true;
            }
        }
        // in case object is not on list return false
        return false;
    }

    public synchronized Map<Tag, Integer> getUseOfTags() {
        return new HashMap<Tag, Integer>(this.tagMap);
    }

    public synchronized BigDecimal getTotalSize() {
        return this.totalSize;
    }

    public synchronized BigDecimal getMaxSizeOfMemory() {
        return this.maxSizeOfMemory;
    }

    @Override
    public synchronized void meldeAn(Observer observer) {
        if (this.beobachterList == null) {
            this.beobachterList = new LinkedList<>();
        }
        this.beobachterList.add(observer);
    }

    @Override
    public synchronized void meldeAb(Observer observer) {
        if (beobachterList == null) return;
        this.beobachterList.remove(observer);
    }

    @Override
    public synchronized void benachrichtige() {
        if (beobachterList == null) return;
        for (Observer observer : this.beobachterList) observer.aktualisiere();
    }

    /*
    @Override
    public void saveMediaFiles(String pathToFileFromScrFolder) {
        JOS.serialize(pathToFileFromScrFolder, mediaList);
    }

    @Override
    public void saveProducer(String pathToFileFromScrFolder) {
        JOS.serialize(pathToFileFromScrFolder, producerList);
    }

    @Override
    public void loadMediaFiles(String pathToFileFromScrFolder) {
        Collection<AbstractUploadable> mediaListNeu = JOS.deserialize(pathToFileFromScrFolder);
        this.mediaList = (LinkedList<AbstractUploadable>) mediaListNeu;

        double newTotalSize = 0;
        for (int i = 0; i < mediaList.size(); i++) {
            BigDecimal size = mediaList.get(i).getSize();
            double newSize = size.doubleValue() + newTotalSize;
            newTotalSize = newSize;
        }
        this.totalSize = new BigDecimal(newTotalSize);
        //datenbank-grösse wird dabei nicht mitpersistiert
    }

    @Override
    public void loadProducer(String pathToFileFromScrFolder) {
        Collection<UploaderSuper> uploaderListNeu = JOS.deserialize(pathToFileFromScrFolder);
        this.producerList = (LinkedList<UploaderSuper>) uploaderListNeu;
    }*/
}

