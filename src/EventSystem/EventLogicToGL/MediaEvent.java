package EventSystem.EventLogicToGL;

import mediaDB.Tag;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedList;

public class MediaEvent extends EventObject {
    String mediaType;
    String nameOfProducer;
    Collection<Tag> tags;
    BigDecimal bitrate;
    Duration length;
    String optionaleParameter;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */

    public MediaEvent(Object source, String mediaType, String nameOfProducer, Collection<Tag> tags, BigDecimal bitrate,
                      Duration length, String optionaleParameter) {
        super(source);
        this.mediaType = mediaType;
        this.nameOfProducer = nameOfProducer;
        this.tags = tags;
        this.bitrate = bitrate;
        this.length = length;
        this.optionaleParameter = optionaleParameter;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getNameOfProducer() {
        return nameOfProducer;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public BigDecimal getBitrate() {
        return bitrate;
    }

    public Duration getLength() {
        return length;
    }

    public String getOptionaleParameter() {
        return optionaleParameter;
    }
}
