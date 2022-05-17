package mediaDB;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

//my prototype of MediaFile -> if something essential changes here - change in other classes as well
public class AudioVideoImpl  extends AbstractUploadable implements AudioVideo {
    //members sorted by constructor, which is sorted by user input
    String mediaType;
    Uploader uploader; // = producer
    Collection<Tag> tagCollection;
    BigDecimal bitrate;
    Duration length;
    BigDecimal size; //besser in Objekten selbst bei unterschiedlicher formel berechnen? access nicht gegeben
    String address;
    Date uploadDate;
    public long accessCount;
    //optionale Parameter:
    int resolution;
    int samplingRate;


    public AudioVideoImpl (String mediaType, Uploader nameOfProducer, Collection<Tag> tags, BigDecimal bitrate, Duration length,
                           String optionaleParameter, BigDecimal size, String address, Date uploadDate) {

        this.mediaType = mediaType;
        this.uploader = nameOfProducer;
        this.tagCollection = tags;
        this.bitrate = bitrate;
        this.length = length;
        this.size = size;
        this.address = address;
        this.uploadDate = uploadDate;

        //split optional parameter into resolution and sampling rate
        String parameter[] = optionaleParameter.split(" ");
        this.resolution = Integer.parseInt(parameter[0]);
        this.samplingRate = Integer.parseInt(parameter[1]);

    }

    @Override
    public String toString() {
        return mediaType.toLowerCase();
    }

    @Override
    public Uploader getUploader() {
        return this.uploader;
    }

    @Override
    public Collection<Tag> getTags() {
        return this.tagCollection;
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
    public String getAddress() {
        return this.address;
    }

    @Override
    public Date getUploadDate() {
        return this.uploadDate;
    }

    @Override
    public long getAccessCount() {
        return this.accessCount;
    }

    @Override
    public int getSamplingRate() {
        return this.samplingRate;
    }

    @Override
    public int getResolution() {
        return this.resolution;
    }

}
