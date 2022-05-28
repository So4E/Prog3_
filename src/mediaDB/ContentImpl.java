package mediaDB;

import java.util.Collection;

public class ContentImpl  extends AbstractUploadable implements Content {
    String address;
    Collection<Tag> tagCollection;

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

}
