package marsrover.images.mdkinsey.types;

import java.util.Arrays;

public interface types {
    enum ROVER{
        Curiosity("Curiosity", ROVER_CAMERA.FHAZ, ROVER_CAMERA.RHAZ, ROVER_CAMERA.MAST, ROVER_CAMERA.CHEMCAM,
                  ROVER_CAMERA.MAHLI, ROVER_CAMERA.MARDI, ROVER_CAMERA.NAVCAM),
        Opportunity("Opportunity", ROVER_CAMERA.FHAZ, ROVER_CAMERA.RHAZ, ROVER_CAMERA.NAVCAM, ROVER_CAMERA.PANCAM, ROVER_CAMERA.MINITES),
        Spirit("Spirit", ROVER_CAMERA.FHAZ, ROVER_CAMERA.RHAZ, ROVER_CAMERA.NAVCAM, ROVER_CAMERA.PANCAM, ROVER_CAMERA.MINITES);

        String dbName;
        private final ROVER_CAMERA[] cameras;
        ROVER(String dbName, ROVER_CAMERA ...cameras){

            this.cameras = cameras;
            this.dbName = dbName;
        }
        public ROVER_CAMERA[] getAvailableCamera()
        {
            return Arrays.copyOf(cameras, cameras.length);
        }
        public static String toDB(String roverName){
            String fixed = roverName.substring(0,1).toUpperCase()+roverName.substring(1).toLowerCase();
            return ROVER.valueOf(fixed).name();
        }
    }

    enum ROVER_CAMERA{
        FHAZ("Front Hazard Avoidance Camera"),
        RHAZ("Rear Hazard Avoidance Camera"),
        MAST("Mast Camera"),
        CHEMCAM("Chemistry and Camera Complex"),
        MAHLI("Mars Hand Lens Imager"),
        MARDI("Mars Descent Lens Imager"),
        NAVCAM("Navigation Camera"),
        PANCAM("Panoramic Camera"),
        MINITES("Miniature thermal Emission Spectrometer (Mini-TES)");

        private final String description;
        ROVER_CAMERA(String desc){
            description = desc;
        }
        public String getDescription(){
            return description;
        }

    }
}
