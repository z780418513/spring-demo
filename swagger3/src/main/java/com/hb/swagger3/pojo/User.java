package com.hb.swagger3.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(name="User",description ="用户信息" )
@Data
@AllArgsConstructor
public class User {
    @Schema(name = "name",description = "姓名")
    private String name;
    @Schema(name = "age",description = "年龄")
    private int age;
}
