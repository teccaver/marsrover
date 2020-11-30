package marsrover.images.mdkinsey.services;

import marsrover.images.mdkinsey.converters.RoverJsonToRoverJPA;
import marsrover.images.mdkinsey.domain.Camera;
import marsrover.images.mdkinsey.domain.Image;
import marsrover.images.mdkinsey.domain.Photos;
import marsrover.images.mdkinsey.domain.PhotosJson;
import marsrover.images.mdkinsey.domain.Rover;
import marsrover.images.mdkinsey.domain.RoverManifest;
import marsrover.images.mdkinsey.services.repository.RoverServiceRepo;
import marsrover.images.mdkinsey.types.types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

@Component
@Scope("singleton")
@ConfigurationProperties(prefix = "image.repository")
public class FileCacheManager implements MarsRoverFileStore, ApplicationRunner, ApplicationListener<ApplicationReadyEvent> {

    private String file;
    private String cache;
    protected File dataFile;
    protected File cacheDirectory;

    private boolean initialized = false;

    @Autowired
    ImageIndexService imageService;
//    @Autowired
//    RoverService roverService;
    @Autowired
    RoverJsonToRoverJPA roverJsonConverter;
    @Autowired
    RoverServiceRepo roverServiceRepo;

    @Override
    public boolean roverExists() {
        return false;
    }

    @Override
    public boolean photoExists() {
        return false;
    }

    @Override
    public Image getPhotoById() {
        return null;
    }

    @Override
    public List<Image> getImageByCamera(types.ROVER rover, String camera) {
        return null;
    }

    @Override
    public List<Image> getImageBySolData(types.ROVER rover, String camera, int solDate) {
        return null;
    }

    @Override
    public synchronized void saveSnapShot(Rover rover) {
        //check to see if the path already exists for this rover

    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

            //get the path from the properties
            if (file==null || cache == null) {
                throw new IllegalStateException("Path for the file store should be provided on the command line or within the application properties");
            }
            initializeCache(cache);
            initializeRepository(file);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> sp = args.getOptionValues(STOREPATH);
        if (sp != null && sp.size() > 1) {
            initializeCache(sp.get(1));
            initializeRepository(sp.get(0));
        }

    }

    private void initializeCache(String cacheLocation){
        if (!initialized){
            if (cacheLocation != null && cacheLocation.length()>1){
                cache = cacheLocation;
                cacheDirectory = new File(cache);
                if (cacheDirectory.exists() && cacheDirectory.isDirectory()){
                    //should be in the DB a
                }
                else
                {
                    //create empty
                    cacheDirectory.mkdir();
                }
            }
        }
    }

    private void initializeRepository(String filePath) {
        if (!initialized) {
            if (filePath != null && filePath.length() > 1) {
                file = filePath;
                dataFile = new File(filePath);
                if (dataFile.exists()) {
                    //read it in
                    //rebuild the database
                    //on error rebuild the data base
                }
                else {
                    //fetch the rover
                    //this is the first run
                    Map<types.ROVER, RoverManifest> roverManifest = imageService.getRoverList();
                    for (RoverManifest r : roverManifest.values()) {
                        Rover rover = roverJsonConverter.convert(r.getPhoto_manifest());
                        List<Photos> pjList = rover.getPhotos();
                        //A camera is tied to a specific rover
                        for (Camera c:rover.getCameras()){
                            c.setRover(rover);
                        }
                        //
                        //TODO:will need to set camera and rover on the photos
                        //to track the track all photos available on a day for a camera/rover pairing
                        Rover savedRover = roverServiceRepo.save(rover);
                    }
                }
            }
        }
        initialized = true;
    }


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCache(){return cache;}
    public void setCache(String cache){this.cache = cache;}
}
