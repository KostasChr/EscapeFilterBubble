package gr.ntua.imu.escapefilterbubble.topics.service;

import gr.ntua.imu.escapefilterbubble.topics.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author KostasChr
 */

@Repository
public class TopicService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public static final String COLLECTION_NAME = "topic";

    public void addTopic(Topic topic) {
        if (!mongoTemplate.collectionExists(Topic.class)) {
            mongoTemplate.createCollection(Topic.class);
        }

        mongoTemplate.insert(topic, COLLECTION_NAME);
    }

    public List<Topic> listTopic() {
        return mongoTemplate.findAll(Topic.class, COLLECTION_NAME);
    }

    public void deleteTopic(Topic topic) {
        mongoTemplate.remove(topic, COLLECTION_NAME);
    }

    public void updateTopic(Topic topic) {
        mongoTemplate.insert(topic, COLLECTION_NAME);
    }

}
