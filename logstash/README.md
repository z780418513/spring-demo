# 使用logstash 来上报springboot日志
## 1. 依赖
```xml
 <!-- logstash -->
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.1.1</version>
</dependency>
```
## 2.修改logback.xml ,其中增加上报logstash的日志appender
```xml

<springProperty scope="context" name="appName" source="spring.application.name"/>

        <!--输出到logstash的appender-->
<appender name="logstash" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
<!--可以访问的logstash日志收集端口-->
<destination>127.0.0.1:4560</destination>
<encoder charset="UTF-8" class="net.logstash.logback.encoder.LogstashEncoder">
    <customFields>{"spring.application.name":"${appName}"}</customFields>
</encoder>
</appender>

<root level="info">
<appender-ref ref="logstash"/>
</root>
```

## 3.修改logstash的配置 config/logstash.conf 
```text
input {
  tcp {
    mode => "server"
    host => "0.0.0.0"
    port => 4560
    codec => json_lines
  }
}
output {
  elasticsearch {
    hosts => ["elasticsearch:9200"]
    index => "%{[spring.application.name]}-%{+YYYY.MM.dd}"
  }
}
```
output 表示上报到elasticsearch,索引名为 服务名-年.月.日
