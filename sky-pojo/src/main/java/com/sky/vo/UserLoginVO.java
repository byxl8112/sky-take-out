package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor        //自动生成一个无参构造器
@AllArgsConstructor       //自动生成一个满参构造器
public class UserLoginVO implements Serializable {

    private Long id;
    private String openid;
    private String token;

}
