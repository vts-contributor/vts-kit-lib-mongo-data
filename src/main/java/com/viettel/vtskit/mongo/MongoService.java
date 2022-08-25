package com.viettel.vtskit.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.repository.Near;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@EnableMongoRepositories
public class MongoService {
    @Autowired
    MongoTemplate mongoTemplate;

    public UpdateResult updateAndIncrease(String searchField, String searchKey, String increaseField, int increaseValue, Class entityClass){
        Query query=new Query(Criteria.where(searchField).is(searchKey));
        if(mongoTemplate.find(query, entityClass)!=null){
            Update update=new Update().inc(increaseField, increaseValue);
            return mongoTemplate.updateFirst(query,update,entityClass);
        }
        return null;
    }

    public List fullTextSearch(String searchPhrase, Class entityClass) {
        TextCriteria criteria = TextCriteria
                .forDefaultLanguage()
                .matchingPhrase(searchPhrase);
        Query query = TextQuery.queryText(criteria).sortByScore();
        List list = mongoTemplate.find(query, entityClass);

        return list;
    }

    public List findGeoNear(Double lat, Double lng, Class entityClass) {
        NearQuery nearQuery= NearQuery.near(lat, lng);
        GeoResults geoResults= mongoTemplate.geoNear(nearQuery,entityClass);
        return geoResults.getContent();
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
