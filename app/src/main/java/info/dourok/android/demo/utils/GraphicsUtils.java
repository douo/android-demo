package info.dourok.android.demo.utils;

import android.graphics.Path;

/**
 * Created by tiaolins on 2017/3/8.
 */

public class GraphicsUtils {
    /**
     * 见：http://dourok.info/2009/11/21/draw-a-star
     * 一個生成星星形狀的GeneralPath的方法
     *
     * @param x0            星星外接圓的圓心
     * @param y0            星星外接圓的圓心
     * @param r             外接圓的半徑
     * @param v             決定星星的胖瘦，取值(0,1)，也就是小外接圆等于大外接圆的倍数
     * @param baseRadian    決定星星的转向，弧度
     * @param branchesCount 星星的角數
     * @return
     */
    public static Path pathStar(double x0, double y0, double r, double v, double baseRadian, int branchesCount) {
        double d = 2 * Math.PI / branchesCount;
        double[][] p = new double[branchesCount * 2][2];
        for (int i = 0; i < branchesCount; i++) {
            //计算角顶点，0°开始（也就是第一个顶点在 x 轴），顺时针方向。
            p[i * 2][0] = x0 + r * Math.cos(i * d + baseRadian);
            p[i * 2][1] = y0 + r * Math.sin(i * d + baseRadian);
            //计算凹入角顶点
            p[i * 2 + 1][0] = x0 + v * r * Math.cos((i + 0.5) * d + baseRadian);
            p[i * 2 + 1][1] = y0 + v * r * Math.sin((i + 0.5) * d + baseRadian);
        }
        Path path = new Path();
        path.moveTo((float) p[0][0], (float) p[0][1]);
        for (int i = 1; i < p.length; i++) {
            path.lineTo((float) p[i][0], (float) p[i][1]);
        }
        path.close();
        return path;
    }

    /**
     *
     * 生成正多边形
     *
     * @param x0            外接圓的圓心
     * @param y0            接圓的圓心
     * @param r             外接圓的半徑
     * @param baseRadian    转向，弧度
     * @param sideCount 边數
     * @return
     */
    public static Path pathRegularPolygon(double x0, double y0, double r, double baseRadian, int sideCount) {
        double d = 2 * Math.PI / sideCount;
        double[][] p = new double[sideCount][2];
        for (int i = 0; i < sideCount; i++) {
            //计算角顶点，0°开始（也就是第一个顶点在 x 轴），顺时针方向。
            p[i][0] = x0 + r * Math.cos(i * d + baseRadian);
            p[i][1] = y0 + r * Math.sin(i * d + baseRadian);
        }
        Path path = new Path();
        path.moveTo((float) p[0][0], (float) p[0][1]);
        for (int i = 1; i < p.length; i++) {
            path.lineTo((float) p[i][0], (float) p[i][1]);
        }
        path.close();
        return path;
    }
}
