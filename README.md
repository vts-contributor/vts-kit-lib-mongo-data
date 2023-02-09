# vts-kit-lib-mongo-data
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
    <artifactId>vts-kit-lib-mongo-data</artifactId>
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
MongoCollection<Document> documentMongoCollection = mongoService.createCollection(Class entityClass);

MongoCollection<Document> documentMongoCollection = mongoService.createCollection(String CollectionName);
```
##### Insert data
```java
<T> objectToSave = mongoService.insertData(T objectToSave)

<T> objectToSave mongoService.insertData(T objectToSave, String collectionName)
```
##### Get by field
```java
List<T> listObject = mongoService.getByField(String field,String searchField, Class<T> entityClass)

List<T> listObject = mongoService.getByField(String field,String searchField,  Class<T> entityClass,String collectionName)

List<T> listObject = mongoService.getByFieldAndSort(String field,String searchField, Class<T> entityClass, String properties)
```
##### Delete data
```java
<T> objectDelete = mongoService.deteleByField(String field, String deleteField, Class<T> entityClass)

<T> objectDelete = mongoService.deteleByField(String field, String deleteField, Class<T> entityClass, String collectionName)
```
##### Update data
```java
UpdateResult updateResult = mongoService.updateAllValue(String field, String value, String updateValue, Class entityClass)
```
##### Update and increase value
```java
UpdateResult updateResult = mongoService.updateAndIncrease(String searchField, String searchKey, String increaseField, int increaseValue, Class entityClass)
```
##### Search fulltext
```java
List fullTextSearch(String searchPhrase, Class entityClass)
```
##### Geospatial Support
###### GeoNear
You can use the near query to search for places within a given distance
```java
List<GeoResult<T>> geoResult = mongoService.findGeoNear(Double lat, Double lng, Class entityClass)
```
###### Within Query
The geoWithin query enables us to search for places that fully exist within a given Geometry
* geoWithinCenterSphere
```java
FindIterable<Document> geoResult = mongoService.geoWithinCenterSphere(String entityClass, Double lat, Double lng, Double distanceInRad)
```
* geoWithinBox
```java
FindIterable<Document> geoResult = mongoService.geoWithinBox(String entityClass, Double lowerLeftX, Double lowerLeftY, Double upperRightX, Double upperRightY))
```
* geoWithinPolygon
```java
FindIterable<Document> geoResult = mongoService.geoWithinPolygon(String entityClass, ArrayList<List<Double>> points)
```
###### geoIntersects
The geoIntersects query finds objects that at least intersect with a given Geometry
```java
FindIterable<Document> geoResult = mongoService.geoIntersects(String entityClass, ArrayList<Position> positions)
```

## Aggregation introduce

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

3: Output of `$group` operation will be used as input for `$sort` operation. Operation `$Sort` is responsible for sorting the results (according to the given conditions) and returns the final result.

### Comparison table between SQL and aggregation framework:

<p align="center">
    <img src="/doc/images/Aggregation.PNG"/>
</p>

#### Example

Sample data `Etudiant.class`
```json
{
  "firstName": "W",
  "lastName": "Nguyen Van",
  "age": 25,
  "location": "DN",
  "salary": 22.2
}
```

```java
public void getAllLastNameInLocation() {
    String location = "DN";
    Criteria criteria = Criteria.where("location").is(location);
    Aggregation aggregation = newAggregation(
            Aggregation.match(criteria),
            Aggregation.group("lastName").first("lastName").as("lastName")

    );
    AggregationResults<Etudiant> results = this.mongoTemplate.aggregate(aggregation, "etudiant", Etudiant.class);
    List<String> lastNames = new ArrayList<>();
    for (Etudiant employee : results.getMappedResults()) {
        lastNames.add(employee.getLastName());
    }
    //
}
```
The above code is similar to the following aggregation.
```sql
db.etudiant.aggregate ([ { "$match" : { "location" : "DN"}} , { "$group" : { "_id" : "$lastName" , "lastName" : { "$first" : "$lastName"}}}])
```
The equivalent SQL query in MySQL for the given MongoDB aggregate function would be:
```sql
SELECT MIN(lastName) as lastName 
FROM etudiant 
WHERE location = 'DN' 
GROUP BY lastName;
```
Here, the WHERE clause is equivalent to the $match stage in the MongoDB aggregate function, filtering the records based on the value of location. The GROUP BY clause is equivalent to the $group stage, grouping the records based on the value of lastName. The MIN function is used to return the first value of the lastName field for each group, which is equivalent to the $first operator in the MongoDB aggregate function.
- Criteria : This is a class in the package org.springframework.data.mongodb.core.query that provides many methods to execute queries like WHERE , IS , LT , GT

```java
Criteria criteria = Criteria.where("location").is(location);
```
- This statement is used to create a Criteria containing the condition that the locations in the database must be equal to the location from the input parameter.
```java
Aggregation aggregation = newAggregation(
            Aggregation.match(criteria),
            Aggregation.group("lastName").first("lastName").as("lastName")
    );
```
- The above statement is used to create a Pipeline with the match(criteria) operator with the criteria defined above and group by lastName.
```java
AggregationResults<Etudiant> results = this.mongoTemplate.aggregate(aggregation, "etudiant", Etudiant.class);
```
- Use mongoTemplate to manipulate the database with the aggregation defined above, the collection name is Employees and return Employee.class

### We can also use aggregation in many other cases.
- Count the number of records returned.
```java
Criteria criteria = Criteria.where("location").is(location);
AggregationOperation match = Aggregation.match(criteria);
AggregationOperation group = Aggregation.group("location").count().as("total");
```
- Calculation based on the conditions, specifically in this case I am using to calculate the total salary of people over 23 years old and are in DN.
```java
Criteria criteria = Criteria.where("age").gte(23).and("location").is("DN");
AggregationOperation match = Aggregation.match(criteria);
AggregationOperation group = Aggregation.group("location").sum("salary").as("totalSalary").push("lastName").as("lastName");
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
