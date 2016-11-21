package com.bargetor.nest.common.util;

import com.bargetor.nest.common.ui.Location;

/**
 * Created by Bargetor on 16/4/19.
 *
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。
 * GCJ02坐标系：即火星坐标系，WGS84坐标系经加密后的坐标系。
 * BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系。
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
 *
 * 火星坐标系：
 *   iOS 地图（其实是高德）
 *   Gogole地图
 *   搜搜、阿里云、高德地图
 *  百度坐标系：
 *   当然只有百度地图
 *  WGS84坐标系：
 *   国际标准，谷歌国外地图、osm地图等国外的地图一般都是这个
 */
public class CoordTransformUtil {
    public static final double x_pi = Math.PI * 3000D / 180D;
    /**
     * 长半轴
     */
    public static final double a = 6378245.0;

    /**
     * 扁率
     */
    public static final double ee = 0.00669342162296594323;

    public static Location bd09Togcj02(double lng, double lat){
        double x = lng - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);

        Location location = new Location(gg_lng, gg_lat);
        location.setType(Location.Type.GCJ02);
        return location;
    }


    /**
     * 火星坐标系(GCJ-02)转百度坐标系(BD-09)
     * 谷歌、高德——>百度
     * @param lng
     * @param lat
     * @return
     */
    public static Location gcj02Tobd09(double lng, double lat){
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_pi);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_pi);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;

        Location location = new Location(bd_lng, bd_lat);
        location.setType(Location.Type.BD09);
        return location;
    }

    /**
     * WGS84转GCJ02(火星坐标系)
     * @param lng
     * @param lat
     * @return
     */
    public static Location wgs84Togcj02(double lng, double lat){
        if(outOfChina(lng, lat)){
            Location location = new Location(lng, lat);
            location.setType(Location.Type.GCJ02);
            return location;
        }else {
            double dlat = transformlat(lng - 105.0, lat - 35.0);
            double dlng = transformlng(lng - 105.0, lat - 35.0);
            double radlat = lat / 180.0 * Math.PI;
            double magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            double sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * Math.PI);
            dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * Math.PI);
            double mglat = lat + dlat;
            double mglng = lng + dlng;

            Location location = new Location(mglng, mglat);
            location.setType(Location.Type.GCJ02);
            return location;
        }
    }

    /**
     * GCJ02 转换为 WGS84
     * @param lng
     * @param lat
     * @return
     */
    public static Location gcj02Towgs84(double lng, double lat){
        if(outOfChina(lng, lat)){
            Location location = new Location(lng, lat);
            location.setType(Location.Type.WGS84);
            return location;
        }else {
            double dlat = transformlat(lng - 105.0, lat - 35.0);
            double dlng = transformlng(lng - 105.0, lat - 35.0);
            double radlat = lat / 180.0 * Math.PI;
            double magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            double sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * Math.PI);
            dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * Math.PI);
            double mglat = lat + dlat;
            double mglng = lng + dlng;

            Location location = new Location(mglng, mglat);
            location.setType(Location.Type.WGS84);
            return location;
        }
    }

    /**
     * 判断坐标是否在国内
     * @param lng wgs84
     * @param lat wgs84
     * @return
     */
    public static boolean outOfChina(double lng, double lat){
        if(lng < 72.004 || lng > 137.83447)return true;
        if(lat < 0.8293 || lat > 55.8271)return true;
        return false;
    }

    private static double transformlat(double lng, double lat){
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * Math.PI) + 40.0 * Math.sin(lat / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * Math.PI) + 320 * Math.sin(lat * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformlng(double lng, double lat){
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * Math.PI) + 40.0 * Math.sin(lng / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * Math.PI) + 300.0 * Math.sin(lng / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }
}
