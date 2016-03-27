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
