# vts-kit-ms-mongo-data
-------
This library provides utilities that make it easy to integrate Mongo data into spring boot project

Feature List:
* [Create Collection](#Create-collection)
* [Insert data](#Insert-data)
* [Get by field](#Get-by-field)
* [Delete data](#Delete-data)
* [Update data](#Update-data)
* [Update and increase value](#Update-and-increase-value)
* [Search fulltext](#Search-fulltext)
* [Geospatial support](#Geospatial-Support)
* [Aggregation introduce](#Aggregation-introduce)

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
##### Create collection
```java
mongoService.createCollection(Class entityClass);

mongoService.createCollection(String CollectionName);
```
##### Insert data
```java
mongoService.insertData(T objectToSave)

mongoService.insertData(T objectToSave, String collectionName)
```
##### Get by field
```java
mongoService.getByField(String field,String searchField, Class<T> entityClass)

mongoService.getByField(String field,String searchField,  Class<T> entityClass,String collectionName)

mongoService.getByFieldAndSort(String field,String searchField, Class<T> entityClass, String properties)
```
##### Delete data
```java
mongoService.deteleByField(String field, String deleteField, Class<T> entityClass)

mongoService.deteleByField(String field, String deleteField, Class<T> entityClass, String collectionName)
```
##### Update data
```java
mongoService.updateAllValue(String field, String value, String updateValue, Class entityClass)
```
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
##Aggregation introduce
### Introduction
Aggregation framework is an advanced query of MongoDb that allows to perform calculations, processing and combining from multiple documents (similar to tables in SQL) to produce the necessary information.
### The principle of aggregation
When working with the Aggregation framework, in principle Aggregation will perform processing based on aggregation pipelines. Each step performs a single computation in the input data and generates the output data. To understand better we can look at the processing flow below:

<p align="center">
    <img src="/doc/images/FollowAggregation.PNG"/>
</p>


```java
Some basic Operations in Aggregation :

$project : specify the desired fields to query.

$match : select the desired document to query.

$limit: limit the number of documents

$skip : skip certain documents

$group: group documents according to certain conditions

$sort: sort document

$unwind : perform an expand operation on an array, creating an output document for each value in that array

$out : writes the result after executing on the pipeline to a collection.
```
Here's an example aggregation framework pipeline when following based on the match , group and sort operators:
```java
db.products.aggregate([ {$match: …}, {$group: …}, {$sort: …} ] )
```
The operators to be performed are described as shown below:

<p align="center">
    <img src="/doc/images/FollowAggregationDetails.PNG"/>
</p>
The image above when performing the calculation on the products collection.

1: First the `$match` operation will be executed. `$match` will select certain documents from input.

2: After performing the `$match` operation, the values from the output of `$match` will be used as input for the `$group` operation. This operator is responsible for grouping the outputs according to specific conditions such as sum or average.

3: Output of `$group` operation will be used as input for `$sort` operation. Operation `$Sort` is responsible for sorting the results (according to the given conditions) and returns the final result

###Comparison table between SQL and aggregation framework:

<p align="center">
    <img src="/doc/images/Aggregation.PNG"/>
</p>



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