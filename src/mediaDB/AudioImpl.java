package mediaDB;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class AudioImpl implements Audio {
    String mediaType;
    Uploader uploader;
    Collection<Tag> tagCollection;
    BigDecimal bitrate;
    Duration length;
    BigDecimal size;
    String address;
    Date uploadDate;
    public long accessCount;
    //optionale Parameter
    int samplingRate;

    public AudioImpl(String mediaType, Uploader nameOfProducer, Collection<Tag> tags, BigDecimal bitrate, Duration length,
                     String optionaleParameter, BigDecimal size, String address, Date uploadDate) {

        this.mediaType = mediaType;
        this.uploader = nameOfProducer;
        this.tagCollection = tags;
        this.bitrate = bitrate;
        this.length = length;
        this.size = size;
        this.address = address;
        this.uploadDate = uploadDate;

        //check if optionaleParameter has int for sampling rate, if not setDefault
        int potentialSamplingRate;
        try {
            potentialSamplingRate = Integer.parseInt(optionaleParameter);
        } catch (Exception e) {
            potentialSamplingRate = -1;
        } //if wrong parameters are given -> do set default
        if (potentialSamplingRate >= 0) {
            this.samplingRate = potentialSamplingRate;
        } else {
            //default value
            this.samplingRate = 800;
        }
    }

    public boolean increaseAccessCount() {
        this.accessCount = accessCount + 1;
        return true;
    }

    @Override
    public String toString() {
        return mediaType.toLowerCase();
    }

    @Override
    public int getSamplingRate() {
        return this.samplingRate;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public Collection<Tag> getTags() {
        return this.tagCollection;
    }

    @Override
    public long getAccessCount() {
        return this.accessCount;
    }

    @Override
    public BigDecimal getBitrate() {
        return this.bitrate;
    }

    @Override
    public Duration getLength() {
        return this.length;
    }

    @Override
    public BigDecimal getSize() {
        return this.size;
    }

    @Override
    public Uploader getUploader() {
        return this.uploader;
    }

    @Override
    public Date getUploadDate() {
        return this.uploadDate;
    }
}
