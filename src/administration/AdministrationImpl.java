package administration;

import EventSystem.Observer_InversionOfControl.Observer;
import EventSystem.Observer_InversionOfControl.SubjectForSizeObserver;
import mediaDB.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.*;

public class AdministrationImpl implements Administration, SubjectForSizeObserver, Serializable {
    private LinkedList<AllUploadables> mediaList = new LinkedList<AllUploadables>();
    private BigDecimal maxSizeOfMemory = BigDecimal.valueOf(0);
    private BigDecimal totalSize = BigDecimal.valueOf(0);
    private LinkedList<UploaderSuper> producerList = new LinkedList<UploaderSuper>();  //list of producers is case sensitive
    private Map<Tag, Integer> tagMap = new HashMap<Tag, Integer>();
    private transient List<Observer> observerList;
    //TODO - needed for JBP - delete in case jbp is not implemented completely
    //static final long serialVersionUID = 1L;

    public AdministrationImpl(BigDecimal capacity) {
        //check if input BigDecimal is of a required minimum size for the memory, otherwise throw exception
        BigDecimal minimumSize = new BigDecimal(0);
        if (capacity.compareTo(minimumSize) >= 0) {
            this.maxSizeOfMemory = capacity;
        } else throw new InvalidParameterException();

        //initialize tagMap with all tags from enum
        for (Tag c : Tag.values()) {
            tagMap.put(c, 0);
        }
    }

    //TODO - delete if jbp not completely implemented constructor for JBP
    /*public AdministrationImpl(BigDecimal capacity, LinkedList<UploaderSuper> uploaderlist, HashMap<Tag, Integer> tagMap,
                              LinkedList<AllUploadables> mediaList, BigDecimal totalSize) {
        this.maxSizeOfMemory = capacity;
        this.producerList = uploaderlist;
        this.tagMap = tagMap;
        this.mediaList = mediaList;
        this.totalSize = totalSize;
    }*/

    ////////////////////////////////////////////////////////////////////////////////////
    //--------------------Producer Methods ---------------------------------------------------
    @Override
    public synchronized boolean addProducer(String name) {
        //check if producer is already in list - return false if listed
        if (checkIfProducerIsListed(name) == true) return false;
        //if producer is not in list add him to list and map
        UploaderSuper newProducer = new UploaderImpl(name);
        producerList.add(newProducer);
        newProducer.mediaCount = 0;
        return true;
    }

    @Override
    public synchronized LinkedList<Uploader> getProducerList() {
        return new LinkedList<>(this.producerList);
    }

    @Override
    public synchronized LinkedList<UploaderSuper> listSuperProducer() {
        return new LinkedList<>(this.producerList);
    }

    @Override
    public synchronized boolean deleteProducer(String name) {
        //check if producer is in list - return false if not
        if (checkIfProducerIsListed(name) == false) return false;
            //if listed - delete producer and associated media objects
        else {
            for (int i = 0; i < mediaList.size(); i++) {
                String sameName = mediaList.get(i).getUploader().getName();
                if (sameName.equals(name)) {
                    //with delete method to make sure tags etc. are well persisted
                    this.deleteMedia(mediaList.get(i).getAddress());
                    i = i - 1;
                }
            }
            producerList.remove(getIndexOfProducer(name));
        }
        return true;
    }

    public synchronized boolean checkIfProducerIsListed(String name) {
        for (int i = 0; i < producerList.size(); i++) {
            String checkName = producerList.get(i).getName();
            //if producer is in list return true;
            if (checkName.equals(name)) {
                return true;
            }
        }
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

    //////////////////////////////////////////////////////////////////////////////////
    //------------------- Media Methods----------------------------------------------------
    @Override
    public synchronized boolean addMedia(String mediaType, String nameOfProducer, LinkedList<Tag> tags, BigDecimal bitrate,
                                         Duration length, String optionaleParameter) {
        // if producer is not yet saved (needs to be added first)
        if (!checkIfProducerIsListed(nameOfProducer)) return false;
        UploaderSuper producer = producerList.get(getIndexOfProducer(nameOfProducer)); // else: remember for upload
        //--------- check if maxSize is extended when adding new item
        BigDecimal fakeSize = new BigDecimal(bitrate.longValue());   //set random size for media Object
        BigDecimal newSizeWhenAdding = totalSize.add(fakeSize);
        if (newSizeWhenAdding.compareTo(maxSizeOfMemory) > 0) return false;

        String address = UUID.randomUUID().toString();
        AllUploadables newItem;
        Date uploadDate = new Date(); // Date-Object of this exact day and time

        switch (Mediatype.valueOf(mediaType.toLowerCase())) {
            case audiovideo:
                newItem = new AudioVideoImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, fakeSize, address, uploadDate);
                break;
            case audio:
                newItem = new AudioImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, fakeSize, address, uploadDate);
                break;
            case video:
                newItem = new VideoImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, fakeSize, address, uploadDate);
                break;
            case interactivevideo:
                newItem = new InteractiveVideoImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, fakeSize, address, uploadDate);
                break;
            case licensedaudio:
                newItem = new LicensedAudioImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, fakeSize, address, uploadDate);
                break;
            case licensedvideo:
                newItem = new LicensedVideoImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, fakeSize, address, uploadDate);
                break;
            case licensedaudiovideo:
                newItem = new LicensedAudioVideoImpl(mediaType, producer, tags, bitrate, length,
                        optionaleParameter, fakeSize, address, uploadDate);
                break;
            default:
                return false;
        }

        mediaList.add(newItem);                  //add new item
        totalSize = totalSize.add(fakeSize);         //add its size to total size
        //add plus one in tagMap for used Tags
        if (!tags.isEmpty()) {
            for (int x = 0; x < tags.size(); x++) {
                Tag tagOnList = tags.get(x);
                Arrays.stream(Tag.values()).forEach(c -> {
                    if (c.equals(tagOnList)) {
                        int actual = tagMap.get(c);
                        if (actual >= 0) {
                            tagMap.put(c, actual + 1);
                        }
                    }
                });
            }
        }

        benachrichtige();                       //tell SizeObserver that there were changes made
        producer.increaseMediaCount();
        return true;
    }

    @Override
    public synchronized LinkedList<AllUploadables> getMediaList() {
        return new LinkedList<>(this.mediaList);
    }

    @Override
    public synchronized LinkedList<AllUploadables> getMediaList(String type) {
        LinkedList<AllUploadables> thisTypeOfMediaList = new LinkedList<>();
        for (int i = 0; i < mediaList.size(); i++) {
            if (mediaList.get(i).toString().equals(type.toLowerCase())) {
                thisTypeOfMediaList.add(mediaList.get(i));
            }
        }
        return thisTypeOfMediaList;
    }

    @Override
    public synchronized boolean changeMedia(String address) {
        for (int i = 0; i < mediaList.size(); i++) {
            //found media file with unique address -> so: increaseAccessCount
            if (mediaList.get(i).getAddress().equals(address)) {
                mediaList.get(i).increaseAccessCount();
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
                //---- and decrease MediaCounter
                int index = getIndexOfProducer(uploaderName);
                if (index >= 0) {
                    if (producerList.get(index).getMediaCount() > 0) {
                        producerList.get(index).decreaseMediaCount();
                    }
                }

                //get size of object and decrease used storage space
                MediaContent thatObject = (MediaContent) object;
                BigDecimal size = thatObject.getSize();
                BigDecimal oldSize = totalSize;
                totalSize = new BigDecimal(oldSize.doubleValue() - size.doubleValue());

                //count minus one in Map of used Tags for all Tags of this mediaFile
                Collection<Tag> tags = thatObject.getTags();
                if (!tags.isEmpty()) {
                    for (Tag c : Tag.values()) {
                        //if tag is saved in object reduce count by 1
                        if (tags.contains(c)) {
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

    @Override
    public synchronized Map<Tag, Integer> getTagMap() {
        return new HashMap<Tag, Integer>(this.tagMap);
    }

    @Override
    public synchronized LinkedList<Tag> getUsedTags() {
        LinkedList<Tag> usedTags = new LinkedList<>();
        for (Tag c : Tag.values()) {
            int actual = this.tagMap.get(c);
            if (actual > 0) {
                usedTags.add(c);
            }
        }
        return usedTags;
    }

    @Override
    public synchronized LinkedList<Tag> getNotUsedTags() {
        LinkedList<Tag> neverUsedTags = new LinkedList<>();
        for (Tag c : Tag.values()) {
            int actual = this.tagMap.get(c);
            if (actual == 0) {
                neverUsedTags.add(c);
            }
        }
        return neverUsedTags;
    }


    public synchronized BigDecimal getTotalSize() {
        return this.totalSize;
    }

    public synchronized BigDecimal getMaxSizeOfMemory() {
        return this.maxSizeOfMemory;
    }

    @Override
    public synchronized void meldeAn(Observer observer) {
        if (this.observerList == null) {
            this.observerList = new LinkedList<>();
        }
        this.observerList.add(observer);
    }

    @Override
    public synchronized void meldeAb(Observer observer) {
        if (observerList == null) return;
        this.observerList.remove(observer);
    }

    @Override
    public synchronized void benachrichtige() {
        if (observerList == null) return;
        for (Observer observer : this.observerList) observer.aktualisiere();
    }

}

