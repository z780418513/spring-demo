package com.hb.swagger3.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(name = "CommonResult", description = "通用返回对象")
public class CommonResult<T> {
    @Schema(name = "code", description = "状态码")
    private long code;

    @Schema(name = "message", description = "提示信息")
    private String message;

    @Schema(name = "data", description = "数据封装")
    private T data;

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(200, "操作成功", data);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(400, message, null);
    }
}
