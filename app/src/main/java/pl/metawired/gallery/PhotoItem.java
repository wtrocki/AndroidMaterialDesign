package pl.metawired.gallery;

/**
 * Used to represent a photo item.
 */
public class PhotoItem {

    private String fullImageUri;

    public String getFullImageUri() {
        return fullImageUri;
    }

    public void setFullImageUri(String fullImageUri) {
        this.fullImageUri = fullImageUri;
    }

    public PhotoItem(String fullImageUri) {
        this.fullImageUri = fullImageUri;
    }
}