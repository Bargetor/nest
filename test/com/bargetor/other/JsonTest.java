package com.bargetor.other;

import com.alibaba.fastjson.JSON;
import com.bargetor.other.bean.Food;
import com.bargetor.service.common.util.JsonUtil;

/**
 * Created by Bargetor on 16/3/17.
 */
public class JsonTest {
    public static void main(String[] args){
        String jsonStr = "{\n" +
                "      \"description\": \" \",\n" +
                "      \"food_id\": 1000,\n" +
                "      \"food_name\": \"\\u732a\\u8089\\u996d\",\n" +
                "      \"has_activity\": 0,\n" +
                "      \"is_featured\": 0,\n" +
                "      \"is_gum\": 0,\n" +
                "      \"is_new\": 0,\n" +
                "      \"is_spicy\": 0,\n" +
                "      \"is_valid\": 1,\n" +
                "      \"num_ratings\": [\n" +
                "        0,\n" +
                "        0,\n" +
                "        0,\n" +
                "        0,\n" +
                "        2\n" +
                "      ],\n" +
                "      \"price\": 19.0,\n" +
                "      \"recent_popularity\": 2,\n" +
                "      \"recent_rating\": 5.0,\n" +
                "      \"restaurant_id\": 11,\n" +
                "      \"restaurant_name\": \"麦当劳\",\n" +
                "      \"stock\": 99999,\n" +
                "      \"image_url\": \"http://www.ele.me/demo.jpg\",\n" +
                "      \"packing_fee\": 1.5\n" +
                "    }";

        Food food = JSON.parseObject(jsonStr, Food.class);

        long startTime1 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            JSON.parseObject(jsonStr, Food.class);
        }
        System.out.println(System.currentTimeMillis() - startTime1);

        JsonUtil.jsonStrToBean(jsonStr, Food.class);

        long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            JsonUtil.jsonStrToBean(jsonStr, Food.class);
        }
        System.out.println(System.currentTimeMillis() - startTime2);

    }
}
