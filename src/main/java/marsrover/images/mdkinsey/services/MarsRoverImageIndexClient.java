package marsrover.images.mdkinsey.services;

import marsrover.images.mdkinsey.domain.Image;
import marsrover.images.mdkinsey.domain.RoverManifest;
import marsrover.images.mdkinsey.domain.RoverPhotoManifest;
import marsrover.images.mdkinsey.services.repository.ImageServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarsRoverImageIndexClient implements ImageIndexService {


    private RestTemplate restTemplate;
    private FileCacheManager cacheManager;
    private JsonParser parser = JsonParserFactory.getJsonParser();
    private ImageServiceRepo imageServiceRepo;


    private static final String PARM_SOL = "sol";
    private static final String PARM_EARTH = "earth_date";
    private static final String PARM_PAGE = "page";
    private static final String PARM_APIKEY = "api_key";
    private static final String PARM_CAMERA = "camera";
    private static final String PHOTO_SERVICE = "photos";

    private String roverUrl = "https://api.nasa.gov/mars-photos/api/v1/rovers/";
    private String roverManifest = "https://api.nasa.gov/mars-photos/api/v1/manifests/";


    @Autowired
    public void setRestTemplate(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }
    @Autowired
    public void setFileCacheManager(FileCacheManager cacheManager){ this.cacheManager = cacheManager;}
    @Autowired
    public void setImageServiceRepo(ImageServiceRepo imageServiceRepo){
        this.imageServiceRepo = imageServiceRepo;
    }

    @Override
    public Map<ROVER, RoverManifest> getRoverList() {
        ROVER[] rovers = ROVER.values();
        Map<ROVER, RoverManifest> roverMap = new HashMap<>();
        Map<String, Object> parms = new HashMap<>();

        for (ROVER rover : rovers) {
            String url = buildRoverURL(roverManifest, rover);
            url = parameterizeUrl(parms, url);
            RoverManifest response = restTemplate.getForObject(url, RoverManifest.class);
            roverMap.put(rover, response);
        }
        return roverMap;
    }

    @Override
    public RoverPhotoManifest getImagesBySolDate(ROVER rover, ROVER_CAMERA camera, int sol_date, int page) {
        Map<String, Object> parms = new HashMap<>();
        if (sol_date>=0) parms.put(PARM_SOL, sol_date);
        if (camera!=null) parms.put(PARM_CAMERA, camera.name().toLowerCase());
        if (page>0) parms.put(PARM_PAGE,page);
        String url = buildRoverURL(roverUrl, rover,PHOTO_SERVICE);
        url = parameterizeUrl(parms, url);
        RoverPhotoManifest photoManifest = restTemplate.getForObject(url, RoverPhotoManifest.class);
        return photoManifest;
    }

    @Override
    public marsrover.images.mdkinsey.domain.RoverPhotoManifest getImagesByEarthDate(ROVER rover, ROVER_CAMERA camera, String earthDate, int page) {
        Map<String, Object> parms = new HashMap<>();
        if (validEarthDate(earthDate)) parms.put(PARM_EARTH, earthDate);
        if (camera!=null) parms.put(PARM_CAMERA, camera.name().toLowerCase());
        if (page>0) parms.put(PARM_PAGE,page);
        String url = buildRoverURL(roverUrl, rover,PHOTO_SERVICE);
        url = parameterizeUrl(parms, url);
        RoverPhotoManifest photoManifest = restTemplate.getForObject(url, RoverPhotoManifest.class);
        return photoManifest;
    }

    @Override
    public List<Image> retrieveImages(List<Image> images){
        File cacheDirectory = cacheManager.cacheDirectory;
        List<Image> updatedImages = new ArrayList<>();
        for (Image i:images){
            String url = i.getImg_src();
            File cachedImageFile = null;
            try {
                cachedImageFile = buildCachedFile(i);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            RequestCallback requestCallback = request -> request.getHeaders()
                                                                .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM,MediaType.IMAGE_JPEG));

            File finalCachedImageFile = cachedImageFile;
            if (!i.isCached() && finalCachedImageFile.exists()){
                //inconsistent data.. reset cache and remove file
                i.setCached(false);
                finalCachedImageFile.delete();
            }
            else if (i.isCached() && !finalCachedImageFile.exists()){
                i.setCached(false);
            }

            MarsResponseHandler responseExtractor = new MarsResponseHandler();
            if (!i.isCached()) {
                responseExtractor.setOriginalUrl(url);
                responseExtractor.setCacheFile(finalCachedImageFile);
                do {
                    if (responseExtractor.getLocation() != null) {
                        url = responseExtractor.getLocation();
                        responseExtractor.clearLocation();
                    }
                    restTemplate.execute(url, HttpMethod.GET, requestCallback, responseExtractor);
                } while (responseExtractor.getLocation() != null);

                i.setCached(true);
                i.setCachedFile(finalCachedImageFile.toString());
                Image updatedImage = imageServiceRepo.save(i);
                updatedImages.add(updatedImage);
            }else{
                System.out.printf("\n\nFile cached. Not retrieved. ID %d photoID %d Cached file: %s from url: %s",i.getId(),i.getPhotoId(),i.getCachedFile(),i.getImg_src());
                updatedImages.add(i);
            }

        }
        return updatedImages;
    }

    private File buildCachedFile(Image image) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(image.getRoverName()).append("_").append(image.getCameraName())
          .append(image.getPhotoId()).append(".jpg");
        Path path = cacheManager.cacheDirectory.toPath();
        Path p = Files.createDirectories(path);
        Path filePath = Paths.get(p.toString(), sb.toString());
        File f = filePath.toFile();
        //f.createNewFile();
        return f;
    }
    private boolean validEarthDate(String earthDate){
        if (earthDate != null){
            //if (earthDate correct format)
            return true;
        }
        return false;
    }

    private String parameterizeUrl(Map<String, Object> parms, String url){
        boolean morethanone = false;
        for (Map.Entry<String, Object> e : parms.entrySet()){
            if (morethanone) {url+="&";}
            url+=e.getKey()+"="+e.getValue().toString();
            morethanone = true;
        }

        url+=morethanone?"&"+PARM_APIKEY+"="+API_KEY:PARM_APIKEY+"="+API_KEY;
        return url;
    }
    private String buildRoverURL(String url, ROVER rover,String ...api){
        url = url + rover.name().toLowerCase();
        for (String service:api){
            url+="/"+service;
        }
        return url+"?";
    }


    @ExceptionHandler(RuntimeException.class)
    public final void handleAllExceptions(RuntimeException ex){
        ex.printStackTrace();
    }

    static class MarsResponseHandler implements ResponseExtractor<Void>{
        private HttpStatus returnCode;
        private File cacheFile;
        private String location;
        private String originalUrl;
        private int retryAttempts = 0;
        private int maxRedireccts = 5;
        private int redirectsLeft = 5;
        public ResultCode resultCode = ResultCode.SUCCESS;

        public enum ResultCode{
            SUCCESS,
            FAILED;
        }

        public void reset(){
            retryAttempts = 0;
            location = null;
            redirectsLeft = maxRedireccts;
            resultCode = ResultCode.SUCCESS;
            cacheFile = null;
            originalUrl = null;

        }
        public void setOriginalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
        }

        public void clearLocation(){location = null;}

        public HttpStatus getReturnCode() {
            return returnCode;
        }

        public void setCacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
        }

        public String getLocation() {
            return location;
        }

        private void printLog(String msg){
            System.out.printf("%s | %s | cache name %s \n\n",msg, originalUrl,cacheFile.getName());
        }

        @Override
        public Void extractData(ClientHttpResponse response) throws IOException {
            HttpStatus returnStatus = response.getStatusCode();
            if (returnStatus.is3xxRedirection()){
                if (redirectsLeft <= 0){
                    printLog("Too many REDIRECTS ");
                    resultCode = ResultCode.FAILED;
                }
                location = response.getHeaders().getLocation().toString();
                printLog(redirectsLeft+" redirects left. processing redirect new location: "+location);
                return null;
            }
            InputStream is = response.getBody();
            System.out.println(response.getStatusText()+"  body length "+is.available());
            if (is.available()==0){
                //retry the request
                if (retryAttempts<3) {
                    retryAttempts++;
                    printLog("No data received | retry attempts "+retryAttempts+" | ");
                    location = originalUrl;
                    cacheFile.delete();
                }
            }else {
                Files.copy(is, cacheFile.toPath());
            }
            return null;

        }
    }

}
