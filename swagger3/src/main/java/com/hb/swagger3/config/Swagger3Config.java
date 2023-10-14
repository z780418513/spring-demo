package com.hb.swagger3.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
/**
 * @author: zhaochengshui
 * @description swagger配置类
 * @date: 2023/10/14 14:07
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Swagger测试Demo",
                version = "V1.0",
                description = "Swagger3接口",
                contact = @Contact(name = "hanbaolaoba", email = "1804323649@qq.com")),
        security = @SecurityRequirement(name = "JWT"))
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "JWT", scheme = "bearer", in = SecuritySchemeIn.HEADER)
public class Swagger3Config {

}



