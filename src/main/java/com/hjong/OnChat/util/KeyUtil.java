package com.hjong.OnChat.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author HJong
 * @version 1.0
 * @date 2024/3/16
 **/

@Component
public class KeyUtil {
    public String generateKey(Integer id) {
        return UUID.randomUUID().toString().replace("-", "") + "." + id;
    }

}
