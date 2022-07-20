package administration;

import mediaDB.AudioVideo;
import mediaDB.Tag;
import mediaDB.Uploader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

public class AudioVideoImpl extends AllUploadables implements AudioVideo {

    public AudioVideoImpl(String mediaType, Uploader nameOfProducer, Collection<Tag> tags, BigDecimal bitrate, Duration length,
                          String optionaleParameter, BigDecimal size, String address, Date uploadDate) {

        this.mediaType = mediaType;
        this.uploader = nameOfProducer;
        this.tagCollection = tags;
        this.bitrate = bitrate;
        this.length = length;
        this.size = size;
        this.address = address;
        this.uploadDate = uploadDate;

        //split optional parameter into samplingRate, resolution
        String parameter[] = optionaleParameter.split(" ");
        try {
            this.samplingRate = Integer.parseInt(parameter[0]);
        } catch (Exception e) {
            this.samplingRate = 12; //setDefault
        }
        try {
            this.resolution = Integer.parseInt(parameter[1]);
        } catch (Exception b) {
            this.resolution = 12; //setDefault
        }
    }
}
