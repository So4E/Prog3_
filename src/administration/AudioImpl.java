package administration;

import mediaDB.Audio;
import mediaDB.Tag;
import mediaDB.Uploader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

public class AudioImpl extends AllUploadables implements Audio {

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


        //try to set sampling rate from optional parameter
        try {
            this.samplingRate = Integer.parseInt(optionaleParameter);
        } catch (Exception e) {
            this.samplingRate = 12; //setDefault
        }
    }
}