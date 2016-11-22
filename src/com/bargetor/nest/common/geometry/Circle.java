package com.bargetor.nest.common.geometry;

/**
 * Created by bargetor on 2016/11/22.
 */
public class Circle extends Geometry<Coordinate> {
    private double radius;

    public Circle(Point center, double radius){
        this(center.getX(), center.getY(), radius);
    }

    public Circle(double centerX, double centerY, double radius){
        this.type = Type.Circle;
        this.coordinates = new Coordinate(centerX, centerY);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
