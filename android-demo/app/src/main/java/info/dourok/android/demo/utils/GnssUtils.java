package info.dourok.android.demo.utils;

import android.location.GnssStatus;
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

public class GnssUtils {
    public enum Datum {
        WGS_08,
        GCJ_02,
        BD_09
    }

    public static class Point {
        public final double lat;
        public final double lon;

        public Point(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public Point(double[] p) {
            this.lat = p[0];
            this.lon = p[1];
        }

        @Override
        public String toString() {
            //39.9081728469,116.3867845961
            return String.format(Locale.getDefault(), "%s,%s", lat, lon);
        }

    }

    public static Point transform(Location location, Datum src, Datum dst) {
        return transform(new Point(location.getLatitude(), location.getLongitude()), src, dst);
    }

    public static Point transform(double latitude, double longitude, Datum src, Datum dst) {
        return transform(new Point(latitude, longitude), src, dst);
    }

    /**
     * @param point
     * @param src   src 与 dst 相同 返回原数据
     * @param dst   不支持 dst 为 WGS-08
     * @return
     */
    public static Point transform(Point point, Datum src, Datum dst) {
        if (dst == Datum.WGS_08) {
            throw new IllegalArgumentException("dst not support WGS_08!");
        } else if (src == dst) {
            return point;
        } else if (src == Datum.WGS_08) {
            point = new Point(InternalGpsConverter.transform(point.lat, point.lon)); //WGS -> GCJ
            if (dst == Datum.BD_09) {
                return new Point(InternalGpsConverter.gcjToBd(point.lat, point.lon)); //GCJ -> BD
            }
            return point;
        } else if (src == Datum.GCJ_02) {
            return new Point(InternalGpsConverter.gcjToBd(point.lat, point.lon)); //GCJ -> BD
        } else {
            return new Point(InternalGpsConverter.bdToGcj(point.lat, point.lon)); //BD -> GCJ
        }
    }


    private static final int GLONASS_SVID_OFFSET = 64;
    private static final int BEIDOU_SVID_OFFSET = 200;
    private static final int SBAS_SVID_OFFSET = -87;

    /**
     * copy from {@link android.location.GpsSatellite}
     * @param svid
     * @param constellationType
     * @return
     */
    public static int svidToPrn(int svid, int constellationType) {
        int prn = svid;

        // Other satellites passed through these APIs before GnssSvStatus was availble.
        // GPS, SBAS & QZSS can pass through at their nominally
        // assigned prn number (as long as it fits in the valid 0-255 range below.)
        // Glonass, and Beidou are passed through with the defacto standard offsets
        // Other future constellation reporting (e.g. Galileo) needs to use
        // GnssSvStatus on (N level) HAL & Java layers.
        if (constellationType == GnssStatus.CONSTELLATION_GLONASS) {
            prn += GLONASS_SVID_OFFSET;
        } else if (constellationType == GnssStatus.CONSTELLATION_BEIDOU) {
            prn += BEIDOU_SVID_OFFSET;
        } else if (constellationType == GnssStatus.CONSTELLATION_SBAS) {
            prn += SBAS_SVID_OFFSET;
        }
        return prn;
    }
}

