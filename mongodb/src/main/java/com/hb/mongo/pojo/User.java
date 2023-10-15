package com.hb.mongo.pojo;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

/**
 * @author: zhaochengshui
 * @description
 * @date: 2023/10/15 20:28
 */
@Data
@Document("mall_user") // 文档
public class User {
    @MongoId // 文档中的Id
    private Long id;
    @Field("user_name")
    private String name;
    @Field
    private String pwd;
    @Field
    private Date birth;
    private List<String> hobbyList;
    @Transient
    private Integer age;

    public User(Long id, String name, String pwd, Date birth, List<String> hobbyList, Integer age) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.birth = birth;
        this.hobbyList = hobbyList;
        this.age = age;
    }
    // 必须要私有化构造器
    public User() {
    }
}
