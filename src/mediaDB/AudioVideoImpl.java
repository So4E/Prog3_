package mediaDB;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

//my prototype of MediaFile -> if something essential changes here - change in other classes as well
public class AudioVideoImpl extends AllUploadables implements AudioVideo {

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
        this.holder = nameOfProducer.getName();
        this.optionaleParameter = optionaleParameter;


        //split optional parameter into resolution and sampling rate
        String parameter[] = optionaleParameter.split(" ");
        this.resolution = Integer.parseInt(parameter[0]);
        this.samplingRate = Integer.parseInt(parameter[1]);

    }
}
