package info.dourok.android.demo.utils;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Created by tiaolins on 2017/2/23.
 */

class InternalGpsConverter {
//
// Krasovsky 1940
//
//    a=6378245.0,1/f=298.3
//    b=a*(1-f)
//    ee=(a^2-b^2)/a^2;
    static double a = 6378245.0;
    static double ee = 0.00669342162296594323;

    // World Geodetic System ==> Mars Geodetic System
     public static double[] transform(double lat, double lon) {
        double wgLat = lat;
        double wgLon = lon;

        if (outOfChina(wgLat, wgLon)) {
            return new double[]{lat,lon};
        }

        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * PI;
        double magic = sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * cos(radLat) * PI);

        return new double[]{wgLat + dLat, wgLon + dLon};
    }

    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * sqrt(abs(x));
        ret += (20.0 * sin(6.0 * x * PI) + 20.0 * sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * sin(y * PI) + 40.0 * sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * sin(y / 12.0 * PI) + 320 * sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * sqrt(abs(x));
        ret += (20.0 * sin(6.0 * x * PI) + 20.0 * sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * sin(x * PI) + 40.0 * sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * sin(x / 12.0 * PI) + 300.0 * sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }


    static double X_PI = PI * 3000.0 / 180.0;

    public static double[] gcjToBd(double lat,double lon) {
        double x = lon, y = lat;
        double z = sqrt(x * x + y * y) + 0.00002 * sin(y * X_PI);
        double theta = atan2(y, x) + 0.000003 * cos(x * X_PI);

        double bd_lon = z * cos(theta) + 0.0065;
        double bd_lat = z * sin(theta) + 0.006;
        return new double[]{bd_lat, bd_lon};
    }

    public static double[] bdToGcj(double lat,double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * X_PI);
        double theta = atan2(y, x) - 0.000003 * cos(x * X_PI);

        double gg_lon = z * cos(theta);
        double gg_lat = z * sin(theta);
        return new double[]{gg_lat, gg_lon};
    }
}
