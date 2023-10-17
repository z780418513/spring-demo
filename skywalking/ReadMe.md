# 官网下载 SkyWalking APM
官方文档: https://skywalking.apache.org/docs/main/v9.4.0/readme/
下载地址: https://skywalking.apache.org/downloads/
下载SkyWalking APM和Java Agent,其中SkyWalking APM包含两个部分,一个是服务端,一个是服务端UI.Java Agent是用于客户端,搭配JAVA使用

## SkyWalking APM 使用

### 1.修改UI服务的端口地址 vim /webapp/application.yml  serverPort默认是8080,修改成你需要的

### 2.解压并执行bin 下的 startup.sh

## Java Agent使用

### 启动命令配置

-javaagent:/opt/agent/skywalking-agent.jar -Dskywalking.agent.service_name=sky-test-service
-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:11800

### Java Agent 支持gateway

Java Agent默认是不支持gateway的,他的plugins中不包含gateway的jar包,
解决办法 将optional-plugins中的 apm-spring-cloud-gateway-3.x-plugin-9.0.0.jar 和 apm-spring-webflux-5.x-plugin-9.0.0.jar
拷贝到plugins中去
注:apm-spring-cloud-gateway-3.x-plugin-9.0.0.jar 需要根据你的gateway版本来选择

## 日志采集

```xml
<!--链路追踪日志依赖-->
<dependency>
    <groupId>org.apache.skywalking</groupId>
    <artifactId>apm-toolkit-logback-1.x</artifactId>
    <version>9.0.0</version>
</dependency>
```

logback.xml

```xml

<configuration>
    <!-- 引用 Spring Boot 的 logback 基础配置 -->
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    <!-- 变量 yudao.info.base-package，基础业务包 -->
    <springProperty scope="context" name="yudao.info.base-package" source="yudao.info.base-package"/>
    <!-- 格式化输出：%d 表示日期，%X{tid} SkWalking 链路追踪编号，%thread 表示线程名，%-5level：级别从左显示 5 个字符宽度，%msg：日志消息，%n是换行符 -->
    <property name="PATTERN_DEFAULT"
              value="%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} | %highlight(${LOG_LEVEL_PATTERN:-%5p} ${PID:- }) | %boldYellow(%thread [%tid]) %boldGreen(%-40.40logger{39}) | %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}"/>

    <!-- 控制台 Appender -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">　　　　　
        <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
            <layout class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout">
                <pattern>${PATTERN_DEFAULT}</pattern>
            </layout>
        </encoder>
    </appender>

    <!-- 文件 Appender -->
    <!-- 参考 Spring Boot 的 file-appender.xml 编写 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
            <layout class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout">
                <pattern>${PATTERN_DEFAULT}</pattern>
            </layout>
        </encoder>
        <!-- 日志文件名 -->
        <file>${LOG_FILE}</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- 滚动后的日志文件名 -->
            <fileNamePattern>${LOGBACK_ROLLINGPOLICY_FILE_NAME_PATTERN:-${LOG_FILE}.%d{yyyy-MM-dd}.%i.gz}
            </fileNamePattern>
            <!-- 启动服务时，是否清理历史日志，一般不建议清理 -->
            <cleanHistoryOnStart>${LOGBACK_ROLLINGPOLICY_CLEAN_HISTORY_ON_START:-false}</cleanHistoryOnStart>
            <!-- 日志文件，到达多少容量，进行滚动 -->
            <maxFileSize>${LOGBACK_ROLLINGPOLICY_MAX_FILE_SIZE:-10MB}</maxFileSize>
            <!-- 日志文件的总大小，0 表示不限制 -->
            <totalSizeCap>${LOGBACK_ROLLINGPOLICY_TOTAL_SIZE_CAP:-0}</totalSizeCap>
            <!-- 日志文件的保留天数 -->
            <maxHistory>${LOGBACK_ROLLINGPOLICY_MAX_HISTORY:-30}</maxHistory>
        </rollingPolicy>
    </appender>
    <!-- 异步写入日志，提升性能 -->
    <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志。默认的，如果队列的 80% 已满,则会丢弃 TRACT、DEBUG、INFO 级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度，该值会影响性能。默认值为 256 -->
        <queueSize>256</queueSize>
        <appender-ref ref="FILE"/>
    </appender>

    <!-- SkyWalking GRPC 日志收集，实现日志中心。注意：SkyWalking 8.4.0 版本开始支持 -->
    <appender name="GRPC" class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.log.GRPCLogClientAppender">
        <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
            <layout class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout">
                <pattern>${PATTERN_DEFAULT}</pattern>
            </layout>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="ASYNC"/>
        <appender-ref ref="GRPC"/>
    </root>

</configuration>

```

## 设置es做持久化存储
找到 config/application.yml 修改如下
```yaml
storage:
  selector: ${SW_STORAGE:elasticsearch} # 这边设置为es,并确认es地址和用户名密码
```

## 告警通知 

修改config/alarm-settings.yml ,并修改如下请求地址
```yml
webhooks:
  - http://localhost:8080/alert # 这边是请求地址

```
触发规则后,会请求这边的webhooks地址,具体可以看官方文档,修改具体规则,也可以微信,钉钉告警
https://skywalking.apache.org/docs/main/v9.4.0/en/setup/backend/backend-alarm/

## 自定义链路追踪
```xml
<!--自定义链路追踪依赖-->
<dependency>
    <groupId>org.apache.skywalking</groupId>
    <artifactId>apm-toolkit-trace</artifactId>
    <version>9.0.0</version>
</dependency>
```
使用@Trace @Tag @Tags来自定义链路追踪,保证核心功能的链路
```java
    @Trace
    @Tag(key = "userId", value = "arg[0]")
    @Tag(key = "userId2", value = "arg[1]")
    @Tag(key = "result.userId", value = "returnedObj.userId") //固定写法 必须写成returnedObj.XXXX
    @Tag(key = "result.userId2", value = "returnedObj.userId2")
    public ResultVo say(String userId, String userId2) {
        System.out.println("你好: ~~ " + userId + "~~" + userId2);
        return new ResultVo(userId, userId2);
    }
```
