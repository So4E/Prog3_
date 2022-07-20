package administration;

import mediaDB.Uploader;

import java.io.Serializable;

public class UploaderImpl extends UploaderSuper implements Uploader, Serializable {

    public UploaderImpl (String name){
        this.name = name;
    }




}
