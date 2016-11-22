package com.bargetor.nest.common.geometry;

/**
 * Created by bargetor on 2016/11/22.
 */
public abstract class Geometry<C> {
    protected Type type;
    protected C coordinates;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public C getCoordinates() {
        return coordinates;
    }

    public enum Type{
        Point,
        Polygon,
        Circle
    }
}
