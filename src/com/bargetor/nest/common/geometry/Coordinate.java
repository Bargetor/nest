package com.bargetor.nest.common.geometry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by bargetor on 2016/11/22.
 */
public class Coordinate extends ArrayList<Double> {
    public Coordinate(){
        super(2);
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
        if(this.isEmpty()){
            this.add(x);
            this.add(0D);
        }else {
            this.set(0, x);
        }
    }

    public void setY(double y){
        if(this.isEmpty()){
            this.add(0D);
            this.add(y);
        }else if(this.size() == 1){
            this.add(y);
        }else{
            this.set(1, y);
        }
    }

    public double getX(){
        return this.isEmpty() ? 0D : this.get(0);
    }

    public double getY(){
        return this.size() <= 1 ? 0D : this.get(1);
    }

    @Override
    public String toString() {
        return this.getX() + "," + this.getY();
    }
}
