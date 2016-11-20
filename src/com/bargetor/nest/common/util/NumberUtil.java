package com.bargetor.nest.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bargetor on 2016/11/20.
 */
public class NumberUtil {
    private static Map<Character, Long> chineseUnitMap;
    private static Map<Character, Long> chineseNumberMap;
    private static Map<Character, Long> chinesePartUnitMap;

    static {
        chineseUnitMap = new HashMap<>();
        chineseUnitMap.put('十', 10L);
        chineseUnitMap.put('百', 100L);
        chineseUnitMap.put('千', 1000L);

        chinesePartUnitMap = new HashMap<>();
        chinesePartUnitMap.put('万', 10000L);
        chinesePartUnitMap.put('亿', 100000000L);

        chineseNumberMap = new HashMap<>();
        chineseNumberMap.put('零', 0L);
        chineseNumberMap.put('一', 1L);
        chineseNumberMap.put('二', 2L);
        chineseNumberMap.put('三', 3L);
        chineseNumberMap.put('四', 4L);
        chineseNumberMap.put('五', 5L);
        chineseNumberMap.put('六', 6L);
        chineseNumberMap.put('七', 7L);
        chineseNumberMap.put('八', 8L);
        chineseNumberMap.put('九', 9L);
    }


    public static long chinese2Number(String chinese){
        if(!isChineaseNum(chinese)){
            return Long.parseLong(chinese);
        }

        char[] chineseChars = chinese.toCharArray();
        long result = 0;
        long maxUnit = 1;
        long maxPartUnit = 1;
        int currentPartUnitIndex = chineseChars.length;

        int i = chineseChars.length - 1;

        while (i >= 0){
            long number = getNumberFromChinese(chineseChars[i]);

            long unit = getUnitFromChinese(chineseChars[i]);
            if(unit > 1){
                maxUnit = unit;
            }

            long partUnitOrigin = getPartUnitFromChinese(chineseChars[i]);
            long partUnit = partUnitOrigin;
            if(i + 1 == currentPartUnitIndex){
                partUnit *= maxPartUnit;
            }

            if(partUnit > maxPartUnit){
                maxPartUnit = partUnit;
                //段重置时，单位也重置
                maxUnit = 1;
            }
            if(partUnitOrigin > 1)currentPartUnitIndex = i;

            if(i == 0 && chineseChars[i] == '十'){
                number = 1;
            }

            result += number * maxUnit * maxPartUnit;

            i--;
        }

        return result;
    }

    private static long getNumberFromChinese(Character c){
        if(c == null)return 0;
        Long number = chineseNumberMap.get(c);
        if(number != null){
            return number;
        }else {
            return 0;
        }
    }

    private static long getUnitFromChinese(Character c){
        if(c == null)return 1;
        Long unit = chineseUnitMap.get(c);
        if(unit != null){
            return unit;
        }else {
            return 1;
        }
    }

    private static long getPartUnitFromChinese(Character c){
        if(c == null)return 1;
        Long unit = chinesePartUnitMap.get(c);
        if(unit != null){
            return unit;
        }else {
            return 1;
        }
    }

    private static boolean isChineaseNum(String num){
        try{
            Long.parseLong(num);
            return false;
        }catch(NumberFormatException e){
            return true;
        }
    }

    public static void main(String[] args){
        System.out.println(chinese2Number("十万"));
    }

}
