package administration;

import mediaDB.Content;
import mediaDB.Tag;
import mediaDB.Uploader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;
import java.util.Map;

public interface Administration {

    /**
     * method to add a new producer to producerList
     * @param name of producer
     * @return true if added, fail in case adding failed or name was already in list
     */
     boolean addProducer(String name);

    /**
     * method to get list of Uploader as return (name in capital letters)
     * @return LinkedList of all Uploaders added so far
     */
    LinkedList<Uploader> getProducerList();

    /**
     * method to get list of UploaderSupers
     * @return LinkedList of all UploaderSupers
     */
    LinkedList<UploaderSuper> listSuperProducer();

    /**
     * method to delete certain producer
     * @param name this producer is to be deleted
     * @return true if was deleted, false if couldnt be deleted
     */
    boolean deleteProducer(String name);

    /**
     * method to quickly check if a producer is already listed
     * @param name of producer to check
     * @return true if in list, false if not in list
     */
    boolean checkIfProducerIsListed (String name);

    /**
     * method to get index of producer in producerList
     * @param name of producer to search
     * @return index of producer - if return is -1 producer is not in list
     */
    int getIndexOfProducer (String name);


//------------------media administration ------------------------------------------
    /**
     * method to add a MediaItem
     *
     * @param mediaType
     * @param nameOfProducer
     * @param tags
     * @param bitrate
     * @param length
     * @param optionaleParameter
     * @return true if done, false if adding failed
     */
    boolean addMedia(String mediaType, String nameOfProducer, LinkedList<Tag> tags, BigDecimal bitrate,
                     Duration length, String optionaleParameter);

    /**
     * method to show saved MediaObjects
     * @return list of MediaObjects that will be generated as readable output in UI and CLI
     */
    LinkedList<AllUploadables> getMediaList();

    /**
     * method to return only media files from this type
     * @param type of mediafiles
     * @return list of mediafiles in database of this type
     */
    LinkedList<Content> getMediaList(String type);

    /**
     * method to get all used tags and quantity of their usage
     * @return list of tag as key and integer value as count of how many times this tag was used
     */
    Map<Tag, Integer> getTagMap();

    /**
     * method to get all used Tags from admin
     * @return list of used tags
     */
    LinkedList<Tag> getUsedTags();

    /**
     * method to get all Tags from enum, that were not yet used in admin
     * @return list of these not used tags
     */
    LinkedList<Tag> getNotUsedTags();


        /**
         * method to change Media which will increase AccessCount of a MediaItem
         * @param address unique address/ID of item
         *
         */
    boolean changeMedia(String address);

    /**
     * method to delete a media item from memmory
     * @param address of item to be deleted
     * @return true if item at this address was deleted, false in case item was not found or could not
     * be deleted due to other reason
     */
    boolean deleteMedia(String address);
}
