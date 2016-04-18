package com.bargetor.nest.common.util;

import java.util.*;

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


    /**
     * 获取 list 里有多少个不同的key
     * @param list
     * @param distincter
     * @param <E>
     * @param <K>
     * @return
     */
    public static <E, K>Collection<K> distinct(Collection<E> list, Distincter<E, K> distincter){
        if(isCollectionNull(list))return null;
        if(distincter == null)return null;
        Set<K> distinctSet = new TreeSet<>();
        for (E item: list) {
            K key = distincter.getKey(item);
            if(key == null)continue;
            distinctSet.add(key);
        }
        return distinctSet;
    }



    public interface Distincter<E, K>{
        K getKey(E one);
    }

    /**
     * 将list转换成另一个list
     * @param fromList
     * @param oneToOne
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V>List<V>list2List(List<T> fromList, OneToOne<T, V> oneToOne){
        if(ArrayUtil.isCollectionNull(fromList))return null;
        List<V> toList = new ArrayList<>();
        for(T from : fromList){
            V to = oneToOne.one2One(from);
            if(to == null)continue;
            toList.add(to);
        }
        return toList;
    }

    public interface OneToOne<T, V>{
        public V one2One(T one);
    }


    public static <E, K>Collection<E> gather(Collection<E> list, K key, Gather<E, K> gather){
        if(ArrayUtil.isCollectionNull(list))return null;
        if(key == null)return null;
        if(gather == null)return null;
        List<E> result = new ArrayList<>();

        list.forEach((item) -> {
            K itemKey = gather.getKey(item);
            if(key.equals(itemKey)){
                result.add(item);
            }
        });
        return result;
    }

    public interface Gather<T, V>{
        V getKey(T one);
    }
}
