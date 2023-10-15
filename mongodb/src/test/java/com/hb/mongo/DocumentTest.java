package com.hb.mongo;

import com.hb.mongo.pojo.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author: zhaochengshui
 * @description
 * @date: 2023/10/15 20:27
 */
@SpringBootTest
public class DocumentTest {


    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void insert() {
        User user1 = new User(1L, "小王", "123456", new Date(), Lists.newArrayList("足球,篮球"), 13);
        User user2 = new User(2L, "hanbaolaoba", "454f", new Date(), Lists.newArrayList("排球,游泳"), 13);
        User user3 = new User(3L, "xiao)_3hjds", "fdfd", new Date(), Lists.newArrayList("围棋,篮球"), 100);
        User user4 = new User(4L, "66", "¥jkdfjk", new Date(), Lists.newArrayList("游戏"), 0);
        User user5 = new User(5L, "小黑", "123456", new Date(), Lists.newArrayList("跳高"), 13);

        List<User> list = Lists.list(user2, user3, user4, user5);

        // 数据以及存在会抛异常
//        User insert = mongoTemplate.insert(user1);
//        System.out.println(insert);

        // 批量新增,其中一条已经存在,也会抛异常(事务的)
        Collection<User> insert = mongoTemplate.insert(list, User.class);
        System.out.println(insert);
    }

    @Test
    public void query() {
        // 查询所有
        System.out.println(mongoTemplate.findAll(User.class));
        // 根据id查询
        System.out.println(mongoTemplate.findById(3L, User.class));
        // 查询第一个
        System.out.println(mongoTemplate.findOne(new Query(), User.class));
        // 条件查询 (等值)
        Query query = new Query(Criteria.where("user_name").is("小黑"));
        System.out.println(mongoTemplate.find(query, User.class));
        // 条件查询 (范围)
        Query query2 = new Query(Criteria.where("_id").gt(1L).lt(3));
        System.out.println(mongoTemplate.find(query2, User.class));
        // 条件查询 (正则) 类似于like
        Query query3 = new Query(Criteria.where("hobbyList").regex("球"));
        System.out.println(mongoTemplate.find(query3, User.class));

        System.out.println("===========");

        // 多条件查询 or
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("_id").is(1), Criteria.where("_id").is(2));
        System.out.println(mongoTemplate.find(new Query(criteria), User.class));
        // and
        criteria.andOperator(Criteria.where("hobbyList").regex("球"), Criteria.where("user_name").is("hanbaolaoba"));
        System.out.println(mongoTemplate.find(new Query(criteria), User.class));

        System.out.println("===========");
        // 排序 Query().with(Sort.by(Sort.Order.desc("_id")
        System.out.println(mongoTemplate.find(new Query().with(Sort.by(Sort.Order.desc("_id"))), User.class));
        // 分页 Query().skip(2).limit(5) 等价于mysql limit 2,5
        System.out.println(mongoTemplate.find(new Query().skip(2).limit(5), User.class));

        // 字符串json查询(忽略)

    }

    @Test
    public void update() {
        Query query = new Query(Criteria.where("user_name").regex("小大王"));
        Update update = new Update();
        update.set("pwd", "3290849389");
        // 更新第一条
//        System.out.println(mongoTemplate.updateFirst(query, update, User.class));
        // 更新多条
//        System.out.println(mongoTemplate.updateMulti(query, update, User.class));
        // 如果不存在,就插入一条数据
        System.out.println(mongoTemplate.upsert(query, update, User.class));

    }

    @Test
    public void del() {
        // 删除
        System.out.println(mongoTemplate.remove(new Query(Criteria.where("_id").is(333)), User.class));
    }
}
