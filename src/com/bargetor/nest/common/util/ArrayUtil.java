package com.bargetor.nest.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Bargetor on 16/1/2.
 */
public class ArrayUtil {
    /**
     * 拆分集合
     * @param list
     * @param groupCount
     * @return
     */
    public static <T> List<List<T>> split(List<T> list, int groupCount){
        List<List<T>> result = new ArrayList<>();
        if(list == null)return null;

        if(list.size() <= 0 || groupCount <= 0 || groupCount >= list.size()){
            result.add(list);
            return result;
        }

        int index = 0;
        List<T> group = null;
        for(T object : list){
            if(index % groupCount == 0){
                group = new ArrayList<>();
                result.add(group);
            }
            group.add(object);

            index++;
        }

        return result;
    }

    /**
     * 集合是否为空
     * @param collection
     * @return
     */
    public static boolean isCollectionNull(Collection<?> collection){
        return collection == null || collection.size() <= 0;
    }

    /**
     * 是否包含
     * @param main
     * @param include
     * @return
     */
    public static boolean isInclude(Collection<?> main, Collection<?> include){
        if(ArrayUtil.isCollectionNull(include))return true;
        if(ArrayUtil.isCollectionNull(main))return false;

        for (Object in : include) {
            if(!main.contains(in))return false;
        }

        return true;
    }

    /**
     * 减
     * @param minuend
     * @param subtrahend
     * @param <T>
     * @return
     */
    public static <T>Collection<T> subtract(Collection<T> minuend, Collection<T> subtrahend){
        if(ArrayUtil.isCollectionNull(minuend))return minuend;
        if(ArrayUtil.isCollectionNull(subtrahend))return minuend;

        List<T> result = new ArrayList<>(minuend);
        subtrahend.forEach(sub -> result.remove(sub));
        return result;
    }

//    public static void main(String[] args){
//        List<String> list = new ArrayList<>();
//        for(int i = 0; i < 10; i++){
//            list.add(String.valueOf(i));
//        }
//
//        List<List<String>> result = ArrayUtil.split(list, 55);
//
//        System.out.print(result);
//    }
}
