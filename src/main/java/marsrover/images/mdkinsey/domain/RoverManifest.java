package marsrover.images.mdkinsey.domain;

public class RoverManifest {
    private RoverJson photo_manifest;

    public RoverManifest(){}

    public RoverJson getPhoto_manifest() {
        return photo_manifest;
    }

    public void setPhoto_manifest(RoverJson photo_manifest) {
        this.photo_manifest = photo_manifest;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        return photo_manifest.toString(sb).toString();
    }
}
