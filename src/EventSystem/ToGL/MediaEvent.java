package EventSystem.ToGL;

import mediaDB.Tag;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.EventObject;
import java.util.LinkedList;

public class MediaEvent extends EventObject implements Serializable {
    String mediaType;
    String nameOfProducer;
    LinkedList<Tag> tags;
    BigDecimal bitrate;
    Duration length;
    String optionaleParameter;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */

    public MediaEvent(Object source, String mediaType, String nameOfProducer, LinkedList<Tag> tags, BigDecimal bitrate,
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

    public LinkedList<Tag> getTags() {
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
