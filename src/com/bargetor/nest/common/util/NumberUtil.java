package com.bargetor.nest.common.util;

import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bargetor on 2016/11/20.
 */
public class NumberUtil {
    private static Map<Character, Long> chineseUnitMap;
    private static Map<Character, Long> chineseNumberMap;
    private static Map<Character, Long> chinesePartUnitMap;
    private static Map<Long, String> chineseUnitMapReverse;
    private static Map<Long, String> chineseNumberMapReverse;
    private static Map<Long, String> chinesePartUnitMapReverse;

    static {
        chineseUnitMap = new HashMap<>();
        chineseUnitMap.put('十', 10L);
        chineseUnitMap.put('百', 100L);
        chineseUnitMap.put('千', 1000L);
        chineseUnitMapReverse = new HashMap<>();
        chineseUnitMap.forEach((key, value) -> chineseUnitMapReverse.put(value, String.valueOf(key)));

        chinesePartUnitMap = new HashMap<>();
        chinesePartUnitMap.put('万', 10000L);
        chinesePartUnitMap.put('亿', 100000000L);
        chinesePartUnitMapReverse = new HashMap<>();
        chinesePartUnitMap.forEach((key, value) -> chinesePartUnitMapReverse.put(value, String.valueOf(key)));

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
        chineseNumberMapReverse = new HashMap<>();
        chineseNumberMap.forEach((key, value) -> chineseNumberMapReverse.put(value, String.valueOf(key)));
    }


    public static long chinese2Number(String chinese){
        if(!isChineseNum(chinese)){
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

    public static String number2Chinese(long number){
        String prefix = "";
        if(number < 0){
            prefix = "负";
            number = Math.abs(number);
        }
        if(number == 0)return getChineseNumberFromLong(0L);

        String result = "";
        int numberBits = (int) Math.log10(number);
        String maxUnit = getChineseUnitFormBits(numberBits);
        String maxPartUnit = getChinesePartUnitFormBits(numberBits);

        long last = number;
        long preBits = -1L;
        while (last > 0){
            int currentBits = (int) Math.log10(last);
            long currentUnitNumber = (long) Math.pow(10, currentBits);
            long bitNumber = last / currentUnitNumber;
            String bitNumberChinese = getChineseNumberFromLong(bitNumber);

            String unit = getChineseUnitFormBits(currentBits);
            if(!unit.equals(maxUnit)){
                maxUnit = unit;
            }

            String partUnit = getChinesePartUnitFormBits(currentBits);

            if(!partUnit.equals(maxPartUnit)){
                maxPartUnit = partUnit;
            }


            if(preBits - currentBits > 1){
                result += "零";
            }

            //这里处理的是十一/十万这种情况
            if(result.isEmpty() && bitNumber == 1 && maxUnit.equals("十")){
                result += maxUnit + maxPartUnit;
            }else{
                result += bitNumberChinese + maxUnit + maxPartUnit;
            }

            last = last % currentUnitNumber;
            preBits = currentBits;
        }

        return prefix + result;
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

    private static String getChinesePartUnitFormBits(int bits){
        if(bits < 4)return "";
        if(bits >=4 && bits < 8)return getChinesePartUnitFromLong(10000L);
        if(bits >= 8 && bits < 12)return getChinesePartUnitFromLong(100000000L);
        return getChinesePartUnitFormBits(bits - 8) + getChinesePartUnitFromLong(100000000L);
    }

    private static String getChineseUnitFormBits(int bits){
        int last = bits % 4;
        long key = (long) Math.pow(10, last);
        String unit = chineseUnitMapReverse.get(key);
        return unit == null ? "" : String.valueOf(unit);
    }

    private static String getChineseNumberFromLong(long l){
        return chineseNumberMapReverse.get(l);
    }

    private static String getChineseUnitFromLong(long l){
        return chineseUnitMapReverse.get(l);
    }

    private static String getChinesePartUnitFromLong(long l){
        return chinesePartUnitMapReverse.get(l);
    }

    private static boolean isChineseNum(String num){
        try{
            Long.parseLong(num);
            return false;
        }catch(NumberFormatException e){
            return true;
        }
    }

    public static void main(String[] args){
        System.out.println(chinese2Number("十万"));
        System.out.println((int)Math.log10(999));
        System.out.println(number2Chinese(-11));
    }

}
