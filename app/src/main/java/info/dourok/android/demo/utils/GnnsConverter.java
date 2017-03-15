package info.dourok.android.demo.utils;

import android.location.Location;

import java.util.Locale;

/**
 * 提供 WGS-08 转换到 GCJ-02 和 BD-09
 * <p>
 * 以下数据来源：http://www.gpsspg.com/
 * <p>
 * 谷歌地图：39.9087677478,116.3975780499 (GCJ-02)
 * 百度地图：39.9151190000,116.4039630000 (BD-09)
 * 腾讯高德：39.9087311069,116.3975323161 (GCJ-02)
 * 图吧地图：39.9081728469,116.3867845961
 * 谷歌地球：39.9073728469,116.3913445961 (WGS-84)
 * 北纬N39°54′26.54″ 东经E116°23′28.84″
 * <p>
 * InternalGpsConverter 转换结果：
 * WGS-84: 39.90737166666667,116.39134333333335
 * GCJ-02: 39.90877295795899,116.39758452119253
 * BD-09 : 39.91511648548032,116.40395736265349
 * <p>
 * Created by tiaolins on 2017/2/28.
 */

public class GnnsConverter {
    public enum Coordinate {
        WGS_08,
        GCJ_02,
        BD_09
    }

    public static class Datum {
        public final double lat;
        public final double lon;

        public Datum(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public Datum(double[] p) {
            this.lat = p[0];
            this.lon = p[1];
        }

        @Override
        public String toString() {
            //39.9081728469,116.3867845961
            return String.format(Locale.getDefault(), "%s,%s", lat, lon);
        }

    }

    public static Datum transform(Location location, Coordinate src, Coordinate dst) {
        return transform(new Datum(location.getLatitude(), location.getLongitude()), src, dst);
    }

    public static Datum transform(double latitude, double longitude, Coordinate src, Coordinate dst) {
        return transform(new Datum(latitude, longitude), src, dst);
    }

    /**
     *
     * @param datum
     * @param src src 与 dst 相同 返回原数据
     * @param dst 不支持 dst 为 WGS-08
     * @return
     */
    public static Datum transform(Datum datum, Coordinate src, Coordinate dst) {
        if (dst == Coordinate.WGS_08) {
            throw new IllegalArgumentException("dst not support WGS_08!");
        } else if (src == dst) {
            return datum;
        } else if (src == Coordinate.WGS_08) {
            datum = new Datum(InternalGpsConverter.transform(datum.lat, datum.lon)); //WGS -> GCJ
            if (dst == Coordinate.BD_09) {
                return new Datum(InternalGpsConverter.gcjToBd(datum.lat, datum.lon)); //GCJ -> BD
            }
            return datum;
        } else if (src == Coordinate.GCJ_02) {
            return new Datum(InternalGpsConverter.gcjToBd(datum.lat, datum.lon)); //GCJ -> BD
        } else {
            return new Datum(InternalGpsConverter.bdToGcj(datum.lat, datum.lon)); //BD -> GCJ
        }
    }
}

