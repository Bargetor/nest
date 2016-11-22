package com.bargetor.nest.common.geometry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by bargetor on 2016/11/22.
 */
public class Coordinate extends ArrayList<Double> {
    public Coordinate(){
        this(0D, 0D);
    }

    public Coordinate(double x, double y){
        super(2);
        this.add(x);
        this.add(y);
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Double> c) {
        return false;
    }

    @Override
    public void add(int index, Double element) {
        return;
    }

    public void setX(double x){
        this.set(0, x);
    }

    public void setY(double y){
        this.set(1, y);
    }

    public double getX(){
        return this.get(0);
    }

    public double getY(){
        return this.get(1);
    }
}
