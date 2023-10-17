package com.hb.skywalking.pojo;

import lombok.Data;

@Data
public class ResultVo {
    private String userId;
    private String userId2;

    public ResultVo(String userId, String userId2) {
        this.userId = userId;
        this.userId2 = userId2;
    }
}
