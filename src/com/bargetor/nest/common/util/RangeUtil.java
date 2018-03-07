package com.bargetor.nest.common.util;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bargetor on 2016/11/30.
 */
public class RangeUtil {

    public static <T extends Comparable> boolean isConnectedWithoutEquals(Range<T> main, Range<T> other) {
        return main.lowerEndpoint().compareTo(other.upperEndpoint()) < 0
                && main.upperEndpoint().compareTo(other.lowerEndpoint()) > 0;
    }

    public static <T extends Comparable>List<Range<T>> removeIntersection(Range<T> mainRange, Range<T>[] ranges){
        return removeIntersection(mainRange, ArrayUtil.array2List(ranges));
    }

    public static <T extends Comparable>List<Range<T>> removeIntersection(Range<T> mainRange, List<Range<T>> ranges){
        if(mainRange == null)return null;
        List<Range<T>> result = new ArrayList<>();
        result.add(mainRange);

        if(ranges == null)return result;

        for (Range<T> range : ranges) {
            List<Range<T>> resultTemp = new ArrayList<>(result);
            Iterator<Range<T>> resultIt = resultTemp.iterator();
            while (resultIt.hasNext()){
                Range<T> resultItem = resultIt.next();
                result.remove(resultItem);
                List<Range<T>> lastRange = removeIntersection(resultItem, range);
                if(ArrayUtil.isNull(lastRange))continue;
                result.addAll(lastRange);
            }
        }

        return result;
    }

    /**
     * 去掉mainRange与range的相交部分
     * @param mainRange
     * @param range
     * @return
     */
    public static <T extends Comparable>List<Range<T>> removeIntersection(Range<T> mainRange, Range<T> range){
        if(mainRange == null)return null;
        List<Range<T>> result = new ArrayList<>();

        if(range == null) {
            result.add(mainRange);
            return result;
        }

        if(range.encloses(mainRange))return null;

        Range<T> intersection = null;
        try {
            intersection = mainRange.intersection(range);
            Range<T> r1 = null;
            Range<T> r2 = null;

            //如果包含
            if(mainRange.encloses(range)){
                r1 = Range.range(
                        mainRange.lowerEndpoint(),
                        mainRange.lowerBoundType(),
                        intersection.lowerEndpoint(),
                        intersection.lowerBoundType().equals(BoundType.CLOSED) ? BoundType.OPEN : BoundType.CLOSED
                );
                r2 = Range.range(
                        intersection.upperEndpoint(),
                        intersection.upperBoundType().equals(BoundType.CLOSED) ? BoundType.OPEN : BoundType.CLOSED,
                        mainRange.upperEndpoint(),
                        mainRange.upperBoundType()
                );
            }else{
                BoundType lowerType = BoundType.CLOSED;
                BoundType upperType = BoundType.CLOSED;
                T lower = null;
                T upper = null;

                if(mainRange.lowerEndpoint().compareTo(intersection.lowerEndpoint()) < 0){
                    lower = mainRange.lowerEndpoint();
                    lowerType = mainRange.lowerBoundType();
                    upper = intersection.lowerEndpoint();
                    upperType = intersection.lowerBoundType().equals(BoundType.CLOSED) ? BoundType.OPEN : BoundType.CLOSED;

                }else{
                    lower = intersection.upperEndpoint();
                    lowerType = intersection.upperBoundType().equals(BoundType.CLOSED) ? BoundType.OPEN : BoundType.CLOSED;
                    upper = mainRange.upperEndpoint();
                    upperType = mainRange.upperBoundType();
                }

                r1 = Range.range(lower, lowerType, upper, upperType);
            }

            result.clear();
            if(r1 != null && !r1.isEmpty())result.add(r1);
            if(r2 != null && !r2.isEmpty())result.add(r2);
            return result;


        }catch (IllegalArgumentException e){
            //不相交
            result.clear();
            result.add(mainRange);
            return result;
        }
    }

    public static void main(String[] args){
        Range<Integer> mainRange = Range.closedOpen(-3, 1);
        Range<Integer> range = Range.closed(-3, 1);
        Range<Integer> range2 = Range.closed(-3, -2);

        System.out.println(removeIntersection(mainRange, range));

//        System.out.println(removeIntersection(mainRange, range, range2));
    }
}
