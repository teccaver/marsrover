# marsrover

To build an executable jar with test cases executed:
    mvn package
    
To build an executable jar without executing the test cases:
		mvn package -DskipTests
		
		
Peruse NASA's image library for the Mars Rovers Opportunity, Curiosity and Spirit.
This is a refresher project which makes use of the following technologies:
  - spring boota
  - Javascript
  - Jquery
  - Bootstrap
  - JPA
  - JUnit
  - Thymeleaf
  
This is an example project which retieves images using the NASA open api's for the Mars Rovers.  
It consists of 3 components:
  1. a web client
  2. a spring boot web MVC and JPA repository
  3. a consumer of the NASA API rest services. 
 
 only runs on localhost:8080/

The generalized design is to allow a user to view images obtained by basic search criteria. There are hundreds of thousands of photos accessible via these APIs. The most granular access is for a particular camera on a specific rover on a single day. The information obtained is divided into two sets with one set being descriptive information about a Rover's camera on a given day. The other set being the actual images obtained.

The first set is obtained when the application is initiated. This initialization retrieves information about each of the rovers which defines the boundaries of the criteria to establish searches for sets of images. This defines the actual hardware (rover and specific camera) that an image is associated with. 

Upon establishing a search criteria, additional information is obtained about specific images which include the remote location. This produces a list of images which can be viewed. The list is displayed in the browser and the actual image can be retrieved and viewed by clicking the hyperlink. The image is displayed in a modal dialog that supports traversing the displayed list.

As the images are displayed, they are retrieved via the NASA api and stored in a local file cache. The location of this cache is set using the property image.repository.cache in the application.properties file. The format of the file name for the image is rovername_cameranamephotoId.jpg. The photoId is the unique identifier obtained from NASA. Once downloaded any future attempt to down that file is bypassed and the cached file is used. 

There is only basic validation and error handling. 

The stucture of the controllers and client are to allow expansion of the search criteria into ranges spanning time. This can result in overlapping searches. The page index, altough not currently used, is traversed from client to server to allow enhancement for next page/previous page as the NASA server only retrieves 25 images within a page. The functionality is currently limited to 25 results per search. 

The persistence store is present to allow advanced searches to be performed to reduce the set of images retrieved for any given hardware set and day. This can also be enhaced by including a shutdown hook which dumps the database to a file and restores that file upon startup. Currently the database is initialized fresh with each startup. 
