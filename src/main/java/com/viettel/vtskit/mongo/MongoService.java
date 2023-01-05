package com.viettel.vtskit.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@EnableMongoRepositories
public class MongoService {
    @Autowired
    MongoTemplate mongoTemplate;


    public String createCollection(String collectionName){
        try{
            mongoTemplate.createCollection(collectionName);
            return collectionName;
        }catch (Exception e){
            return e.getMessage();
        }

    }
    public void createCollection(Class entityClass){
        mongoTemplate.createCollection(entityClass);
        return;
    }

    public <T> T insertData(T objectToSave, String collectionName){
        return mongoTemplate.save(objectToSave,collectionName);
    }
    public <T> T insertData(T objectToSave){
        return mongoTemplate.save(objectToSave);
    }

    public <T> List<T> getByField(String field,String searchField, Class<T> entityClass){
        Query query = new Query(Criteria.where(field).is(searchField));
        return mongoTemplate.find(query,entityClass);
    }
    public <T> List<T> getByField(String field,String searchField,  Class<T> entityClass,String collectionName){
        Query query = new Query(Criteria.where(field).is(searchField));
        return mongoTemplate.find(query,entityClass,collectionName);
    }
    public <T> List<T> getByFieldAndSort(String field,String searchField, Class<T> entityClass, String properties){
        Query query = new Query(Criteria.where(field).is(searchField));
        query.with(Sort.by(Sort.Direction.DESC, properties));
        return mongoTemplate.find(query,entityClass);
    }

    public <T> T deteleByField(String field, String deleteField, Class<T> entityClass, String collectionName){
        Query query = new Query(Criteria.where(field).is(deleteField));
        return mongoTemplate.findAndRemove(query,entityClass,collectionName);
    }
    public <T> T deteleByField(String field, String deleteField, Class<T> entityClass){
        Query query = new Query(Criteria.where(field).is(deleteField));
        return mongoTemplate.findAndRemove(query,entityClass);
    }

    public UpdateResult updateAllValue(String field, String value, String updateValue, Class entityClass){
        Query query = new Query(Criteria.where(field).is(value));
        Update update = new Update().set(field,updateValue);
        return mongoTemplate.updateMulti(query,update,entityClass);
    }


    public UpdateResult updateAndIncrease(String searchField, String searchKey, String increaseField, int increaseValue, Class entityClass){
        Query query=new Query(Criteria.where(searchField).is(searchKey));
        if(mongoTemplate.find(query, entityClass)!=null){
            Update update=new Update().inc(increaseField, increaseValue);
            return mongoTemplate.updateFirst(query,update,entityClass);
        }
        return null;
    }

    public List fullTextSearch(String searchPhrase, Class entityClass) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(searchPhrase);
        Query query = TextQuery.queryText(criteria).sortByScore();
        List listEntityClass = mongoTemplate.find(query, entityClass);

        return listEntityClass;
    }



    public <T> List<GeoResult<T>> findGeoNear(GeoJsonPoint geoJsonPoint, Class<T> entityClass) {
        NearQuery nearQuery= NearQuery.near(geoJsonPoint);
        GeoResults geoResults= mongoTemplate.geoNear(nearQuery,entityClass);
        return (List<GeoResult<T>>)geoResults.getContent();
    }

    public <T> List<GeoResult<T>> findGeoNear(Double lat, Double lng, Class<T> entityClass) {
        NearQuery nearQuery= NearQuery.near(lat, lng);
        GeoResults geoResults= mongoTemplate.geoNear(nearQuery, entityClass);
        return (List<GeoResult<T>>)geoResults.getContent();
    }

    public FindIterable<Document> geoWithinCenterSphere(String entityClass, Double lat, Double lng, Double distanceInRad) {
        FindIterable<Document> result = mongoTemplate.getCollection(entityClass).find(
                Filters.geoWithinCenterSphere("location", lat, lng, distanceInRad));
        return result;
    }

    public FindIterable<Document> geoWithinBox(String entityClass, Double lowerLeftX, Double lowerLeftY, Double upperRightX, Double upperRightY) {
        FindIterable<Document> result = mongoTemplate.getCollection(entityClass).find(
                Filters.geoWithinBox("location", lowerLeftX, lowerLeftY, upperRightX, upperRightY));
        return result;
    }
    public FindIterable<Document> geoWithinPolygon(String entityClass, ArrayList<List<Double>> points) {
        FindIterable<Document> result = mongoTemplate.getCollection(entityClass).find(
                Filters.geoWithinPolygon("location", points));
        return result;
    }

    public FindIterable<Document> geoIntersects(String entityClass, ArrayList<Position> positions) {
        Polygon geometry=new Polygon(positions);
        FindIterable<Document> result = mongoTemplate.getCollection(entityClass).find(
                Filters.geoIntersects("location", geometry));
        return result;
    }
}
