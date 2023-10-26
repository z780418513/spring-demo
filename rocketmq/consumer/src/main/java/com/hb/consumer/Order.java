package com.hb.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/3/25
 */
@Data
@AllArgsConstructor
public class Order {
    private Long id;
    private String orderName;
    private String channel;

}
