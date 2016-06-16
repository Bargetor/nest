package com.bargetor.nest.common.util;

import com.bargetor.nest.forkjoin.ForkJoinManager;
import org.apache.commons.collections.ArrayStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Bargetor on 16/1/2.
 */
public class ArrayUtil {

    public static <T> List<T> array2List(T[] array){
        if(array == null)return null;
        List<T> list = new ArrayList<>();
        for(T item : array){
            list.add(item);
        }
        return list;
    }


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

    public static boolean isCollectionNotNull(Collection<?> collection){
        return !isCollectionNull(collection);
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
     * @param minuend 被减数
     * @param subtrahend 减数
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
    public static <E, K>Map<K, List<E>> distinct(Collection<E> list, Distincter<E, K> distincter){
        if(isCollectionNull(list))return null;
        if(distincter == null)return null;
        Map<K, List<E>> distinctMap = new HashMap<>();
        for (E item: list) {
            K key = distincter.getKey(item);
            if(key == null)continue;

            List<E> itemList = distinctMap.get(key);
            if(itemList == null){
                itemList = new ArrayList<>();
                distinctMap.put(key, itemList);
            }
            itemList.add(item);
        }
        return distinctMap;
    }



    public interface Distincter<E, K>{
        K getKey(E one);
    }

    public static <T>void listForeach(Collection<T> list, Consumer<T> action){
        if(isCollectionNull(list))return;
        Stream<T> stream = list.parallelStream();
        stream.forEach(action);
    }

    /**
     * 将list转换成另一个list
     * @param fromList
     * @param oneToOne
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V>List<V>list2List(Collection<T> fromList, OneToOne<T, V> oneToOne){
        return list2List(fromList, oneToOne, false);
    }

    /**
     * 将list转换成另一个list
     * @param fromList
     * @param oneToOne
     * @param isParallelStream 是否使用平行流处理
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V>List<V>list2List(Collection<T> fromList, OneToOne<T, V> oneToOne, boolean isParallelStream){
        if(ArrayUtil.isCollectionNull(fromList))return null;


        if(isParallelStream){
//            Stream<T> stream = fromList.parallelStream();
//            stream.forEach(action);
            return ForkJoinManager.getInstance().parallelTask(fromList, one -> oneToOne.one2One(one));
        }else{
            List<V> toList = new ArrayList<>();
            fromList.forEach(one -> {
                V to = oneToOne.one2One(one);
                if(to == null)return;
                toList.add(to);
            });
            return toList;
        }
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
