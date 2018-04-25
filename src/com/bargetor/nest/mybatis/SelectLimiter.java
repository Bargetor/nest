package com.bargetor.nest.mybatis;

/**
 * Created by bargetor on 2017/1/9.
 */
public class SelectLimiter {
    private int start;
    private int end;
    private int size;

    public static SelectLimiter build(int pageNum, int pageSize){
        if(pageNum < 0 || pageSize <= 0)return null;

        int limitStart = pageNum * pageSize;
        int limitEnd = limitStart + pageSize;

        return new SelectLimiter(limitStart, limitEnd);
    }

    public SelectLimiter(int limitStart, int limitEnd){
        this.start = limitStart;
        this.end = limitEnd;
        this.size = limitEnd - limitStart;
        if (this.size < 0) this.size = 0;
    }

    public boolean isInvalid(){
        return this.size <= 0 || this.start >= this.end || this.start < 0 || this.end < 0;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
