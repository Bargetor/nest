package com.bargetor.nest.common.util;

import com.bargetor.nest.forkjoin.ForkJoinManager;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Bargetor on 16/1/2.
 */
public class ArrayUtil {

    public static <T>T get(Collection<T> c, int index){
        if(index < 0)return null;
        if(c == null)return null;
        if(index >= c.size())return null;

        Iterator<T> it = c.iterator();
        for (int i = 0; i < index; i++) {
            it.next();
        }
        return it.next();
    }

    /**
     * 获取在集合中与@param obj相同的一个
     * @param c
     * @param obj
     * @param <T>
     * @return
     */
    public static <T>T get(Collection<T> c, T obj){
        if(isNull(c))return null;
        if(obj == null)return null;

        Iterator<T> it = c.iterator();
        while (it.hasNext()){
            T one = it.next();
            if(one == null)continue;
            if(one.equals(obj))return one;
        }
        return null;
    }

    public static <T>T first(Collection<T> c){
        return get(c, 0);
    }

    public static <T>T last(Collection<T> c){
        return get(c, c.size() - 1);
    }


    public static <T>List<T> randomValue(Collection<T> c, int count){
        if(isNull(c) || count <= 0)return null;
        List<T> toBeRandom = new ArrayList<>(c);
        if(count >= toBeRandom.size())return toBeRandom;

        List<T> values = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            T value = randomValue(toBeRandom);
            values.add(value);
            toBeRandom.remove(value);
            if(isNull(toBeRandom))break;
        }

        return values;
    }

    public static <T>T randomValue(Collection<T> c){
        if(isNull(c))return null;
        int randomIndex = RandomUtil.randomIntByInterval(0, c.size());
        Iterator<T> it = c.iterator();
        for (int i = 0; i < randomIndex; i++) {
            it.next();
        }

        return it.next();
    }

    public static <T>List<T> addAll(Collection<? extends Collection<T>> cc){
        if(isNull(cc))return null;
        return add(list2Array(cc));
    }

    public static <T> List<T> add(Collection<T>... cs){
        if(cs == null)return null;
        if(cs.length <= 0)return null;
        List<T> result = new ArrayList<>();

        for (Collection<T> c : cs) {
            if(c != null)result.addAll(c);
        }

        return result;
    }

    public static <T> List<T> array2List(T[] array){
        if(array == null)return null;
        List<T> list = new ArrayList<>();
        for(T item : array){
            list.add(item);
        }
        return list;
    }

    public static <T>T[] list2Array(Collection<T> c){
        if(isNull(c))return null;
        T obj = c.iterator().next();

        Class<T> clazz = (Class<T>) obj.getClass();
        T[] targetArray = (T[]) Array.newInstance(clazz, c.size());

        return c.toArray(targetArray);
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
     * 最终的取值为[from, to]
     * @param list
     * @param from
     * @param to
     * @param <T>
     * @return
     */
    public static <T>List<T> subList(List<T> list, int from, int to){
        if(isNull(list))return null;
        if(from < 0 || to < 0)return null;
        int start = from >= list.size() ? list.size() - 1 : from;
        start = start < 0 ? 0 : start;

        int end = to >= list.size() ? list.size() - 1 : to;
        end = end < 0 ? 0 : end;
        end += 1;

        return list.subList(start, end);
    }

    /**
     * 集合是否为空
     * @param collection
     * @return
     */
    public static boolean isNull(Collection<?> collection){
        return collection == null || collection.size() <= 0;
    }

    public static boolean isNotNull(Collection<?> collection){
        return !isNull(collection);
    }

    /**
     * 是否包含
     * @param main
     * @param include
     * @return
     */
    public static boolean isInclude(Collection<?> main, Collection<?> include){
        if(ArrayUtil.isNull(include))return true;
        if(ArrayUtil.isNull(main))return false;

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
        if(ArrayUtil.isNull(minuend))return minuend;
        if(ArrayUtil.isNull(subtrahend))return minuend;

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
        if(isNull(list))return null;
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
        if(isNull(list))return;
        //这里需要删除集合中的空值，否则平行流会报错
        trim(list);
        Stream<T> stream = list.parallelStream();
        stream.forEach(action);
    }

    public static <T>void trim(Collection<T> list){
        if(isNull(list))return;
        Iterator<T> it = list.iterator();
        while (it.hasNext()){
            T one = it.next();
            if(one == null)it.remove();
        }
    }

    public static <T>void foreachByIndex(Collection<T> list, ConsumerByIndex<T> action){
        if(isNull(list) || action == null)return;

        Iterator<T> it = list.iterator();
        int index = 0;
        while (it.hasNext()){
            T one = it.next();
            action.foreach(index, one);
            index++;
        }
    }

    public interface ConsumerByIndex<T>{
        public void foreach(int index, T one);
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
        if(ArrayUtil.isNull(fromList))return null;

        if(isParallelStream){
            return ForkJoinManager.getInstance().parallelTask(fromList, one -> oneToOne.one2One(one));
        }else{
            List<V> toList = new ArrayList<>();
            fromList.forEach(one -> {
                if(one == null)return;
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
        if(ArrayUtil.isNull(list))return null;
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

    public static <T>List<T> filter(Collection<T> c, ArrayFilter<T> filter){
        if(isNull(c))return null;
        if(filter == null)return new ArrayList<>(c);

        List<T> filterC = new CopyOnWriteArrayList<>();
        listForeach(c, one -> {
            if(one == null)return;
            if(filter.filter(one))filterC.add(one);
        });

        return filterC;
    }

    public interface  ArrayFilter<T>{
        boolean filter(T one);
    }


    public static void main(String[] args){
        List<Integer> intList = new ArrayList<>();

        intList.add(0);
        intList.add(1);
        intList.add(2);

        System.out.println(System.currentTimeMillis());
        System.out.println(randomValue(intList));
        System.out.println(System.currentTimeMillis());

        Set<Integer> set = new LinkedHashSet<>();
        set.add(1);
        set.add(2);

        System.out.println(get(set, 1));
    }
}
