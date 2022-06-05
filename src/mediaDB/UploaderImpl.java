package mediaDB;

public class UploaderImpl implements Uploader {
    String name;
    int mediaCount;

    public UploaderImpl (String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getMediaCount() {
        return this.mediaCount;
    }

    public boolean increaseMediaCount(){
        this.mediaCount = mediaCount + 1;
        return true;
    }

    public boolean decreaseMediaCount() {
        this.mediaCount = this.mediaCount -1;
        return true;
    }
}
