# vts-kit-ms-mongo-data
-------
This library provides utilities that make it easy to integrate Mongo data into spring boot project

Feature List:
* [Update and increase value](#Update-and-increase-value)
* [Search fulltext](#Search-fulltext)
* [Geospatial support](#Geospatial-Support)

Quick start
-------
* Just add the dependency to an existing Spring Boot project
```xml
<dependency>
    <groupId>com.atviettelsolutions</groupId>
    <artifactId>vts-kit-ms-mongo-data</artifactId>
    <version>1.0.0</version>
</dependency>
```

* Then, add the following properties to your `application-*.yml` file.
```yaml
vtskit:
  mongodb:
    uri: mongodb://localhost:27017
    database: testDB
```

Usage
-------
We wrapped several function: 
First at all, you must inject MongoService
```java
@Autowired
MongoService mongoService;
```
to use our function.
##### Update and increase value
```java
UpdateResult updateAndIncrease(String searchField, String searchKey, String increaseField, int increaseValue, Class entityClass)
```
##### Search fulltext
```java
List fullTextSearch(String searchPhrase, Class entityClass)
```
##### Geospatial Support
###### GeoNear
You can use the near query to search for places within a given distance
```java
List findGeoNear(Double lat, Double lng, Class entityClass)
```
###### Within Query
The geoWithin query enables us to search for places that fully exist within a given Geometry
* geoWithinCenterSphere
```java
FindIterable<Document> geoWithinCenterSphere(String entityClass, Double lat, Double lng, Double distanceInRad)
```
* geoWithinBox
```java
FindIterable<Document> geoWithinBox(String entityClass, Double lowerLeftX, Double lowerLeftY, Double upperRightX, Double upperRightY))
```
* geoWithinPolygon
```java
FindIterable<Document> geoWithinPolygon(String entityClass, ArrayList<List<Double>> points)
```
###### geoIntersects
The geoIntersects query finds objects that at least intersect with a given Geometry
```java
FindIterable<Document> geoIntersects(String entityClass, ArrayList<Position> positions)
```
Build
-------
* Build with Unittest
```shell script
mvn clean install
```

* Build without Unittest
```shell script
mvn clean install -DskipTests
```

Contribute
-------
Please refer [Contributors Guide](CONTRIBUTING.md)

License
-------
This code is under the [MIT License](https://opensource.org/licenses/MIT).

See the [LICENSE](LICENSE) file for required notices and attributions.