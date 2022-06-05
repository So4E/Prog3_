package mediaDB;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

public class AudioImpl extends AbstractUploadable implements Audio {

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
}