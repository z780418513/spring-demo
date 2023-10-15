package com.hb.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

@SpringBootTest
class CollectionTest {
    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    void createCollection() {
        // mongo 如果集合存在,就会报错,创建前需判断
        if (!mongoTemplate.collectionExists("user_2")) {
            MongoCollection<Document> user = mongoTemplate.createCollection("user_2");
            System.out.println(user);
        }
    }

    @Test
    void dropCollection() {
        // 删除集合
        mongoTemplate.dropCollection("user_2");
    }

    @Test
    void getCollection() {
        MongoCollection<Document> user2 = mongoTemplate.getCollection("user_2");
        System.out.println(user2);
    }


}
