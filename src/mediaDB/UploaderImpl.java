package mediaDB;

public class UploaderImpl implements Uploader {
    String name;

    public UploaderImpl (String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }


}
