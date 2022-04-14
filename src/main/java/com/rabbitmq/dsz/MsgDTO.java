package com.rabbitmq.dsz;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/14 19:05
 * @Description:
 */
@Data
@AllArgsConstructor
public class MsgDTO {
    private String name;
    private String address;
}
