package com.njwuqi.rollcall.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class Distance {
    double lng0;
    double lat0;
    double lng1;
    double lat1;
    public  Distance(double lng_0, double lat_0, double lng_1, double lat_1) {
        lng0 = lng_0;
        lat0 = lat_0;
        lng1 = lng_1;
        lat1 = lat_1;
    }
    public double getDistance() {
        // //121.717594,31.12055    121.817629,31.090867
        GlobalCoordinates source = new GlobalCoordinates(lat0, lng0);
        GlobalCoordinates target = new GlobalCoordinates(lat1, lng1);

        double meter = getDistanceMeter(source, target, Ellipsoid.WGS84);
        return meter;
    }

    public static double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid) {
        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);
        return geoCurve.getEllipsoidalDistance();
    }

}
