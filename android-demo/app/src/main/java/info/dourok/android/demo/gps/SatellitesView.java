package info.dourok.android.demo.gps;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pools;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

import info.dourok.android.demo.utils.GnssUtils;
import info.dourok.android.demo.utils.GraphicsUtils;

/**
 * Created by tiaolins on 2017/3/1.
 */

public class SatellitesView extends View {
    public static final int DEFAULT_BACKGROUND_COLOR = 0xFFFFFFFF;
    public static final int DEFAULT_SIZE = 200;
    private static String TAG = "SatellitesView";
    private GnssStatus mGnssStatus;
    private ArrayList<SatelliteWrapper> mSatellites;
    private ArrayList<SatelliteWrapper> tempWrapper;
    private Paint mCompassPaint;
    private Paint mAuxiliaryPaint;
    private Paint mSatellitePaint;
    private boolean showPrn;
    private boolean showSnr;
    private int mCompassColor = DEFAULT_BACKGROUND_COLOR;
    private int mSatelliteColor = 0xFFFF0000;
    private int satelliteRadius;
    private boolean forceSquare;
    private int centerX;
    private int centerY;
    private int compassRadius;
    private GestureDetectorCompat mGestureDetector;
    private RectF rectF = new RectF();

    private boolean mSatelliteSelectable;
    private OnSatelliteSelectedListener mSatelliteSelectedListener;


    private Path[] constellationPaths;


    public interface OnSatelliteSelectedListener {
        void onSatelliteSelected(GpsSatellite satellite);

        void onSatelliteUnSelected(GpsSatellite satellite);
    }

    public SatellitesView(Context context) {
        this(context, null, 0);
    }

    public SatellitesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatellitesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SatellitesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setSatelliteSelectable(true);
        setShowPrn(true);
        setShowSnr(false);

        mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                boolean notFound = true;
                SatelliteWrapper preSelectedWrapper = null;
                SatelliteWrapper selectedWrapper = null;
                for (SatelliteWrapper wrapper : mSatellites) {
                    if (wrapper.selected) {
                        preSelectedWrapper = wrapper;
                    }
                    wrapper.selected = false;
                    if (notFound) {
                        if (wrapper.contains(e.getX() - centerX, e.getY() - centerY)) {
                            Log.d(TAG, (e.getX() - centerX) + ", " + (e.getY() - centerY));
                            wrapper.selected = true;
                            notFound = false;
                            selectedWrapper = wrapper;
                        }
                    }
                }
                if (selectedWrapper != preSelectedWrapper) {
                    if (selectedWrapper != null) {
                        notifySatelliteSelected(selectedWrapper.satellite);
                    }
                    if (preSelectedWrapper != null) {
                        notifySatelliteUnselected(preSelectedWrapper.satellite);
                    }
                }
                invalidate();
                return true;
            }
        });

        mSatellites = new ArrayList<>(20);
        tempWrapper = new ArrayList<>(20);

        DashPathEffect pathEffect = new DashPathEffect(new float[]{10, 5,}, 0);
        mCompassPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mCompassPaint.setStyle(Paint.Style.FILL);
        mCompassPaint.setColor(mCompassColor);

        mAuxiliaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mAuxiliaryPaint.setStyle(Paint.Style.STROKE);
        mAuxiliaryPaint.setPathEffect(pathEffect);
        mAuxiliaryPaint.setColor(0x88000000);

        mSatellitePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mSatellitePaint.setColor(mSatelliteColor);

    }


    private void initPath() {
        constellationPaths = new Path[7];
        //unknown
        Path unknown = new Path();
        Rect rect = new Rect();
        char[] unknownChar = new char[]{'?'};
        float src = mSatellitePaint.getTextSize();
        mSatellitePaint.setTextSize(satelliteRadius);
        mSatellitePaint.getTextBounds(unknownChar, 0, 1, rect);
        mSatellitePaint.getTextPath(unknownChar, 0, 1, -rect.width() / 2, rect.height() / 2, unknown);
        mSatellitePaint.setTextSize(src);

        //gps
        Path gps = new Path();
        gps.addCircle(0, 0, satelliteRadius, Path.Direction.CW);

        constellationPaths[0] = unknown; // unknown
        constellationPaths[1] = gps; // GPS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            constellationPaths[GnssStatus.CONSTELLATION_GALILEO] = GraphicsUtils.pathStar(0, 0, satelliteRadius, 1f / 3, 0, 4);
            constellationPaths[GnssStatus.CONSTELLATION_BEIDOU] = GraphicsUtils.pathStar(0, 0, satelliteRadius, 0.5f, 0, 5);
            constellationPaths[GnssStatus.CONSTELLATION_GLONASS] = GraphicsUtils.pathRegularPolygon(0, 0, satelliteRadius, 0, 4);
            constellationPaths[GnssStatus.CONSTELLATION_QZSS] = GraphicsUtils.pathRegularPolygon(0, 0, satelliteRadius, 0, 5);
            constellationPaths[GnssStatus.CONSTELLATION_SBAS] = GraphicsUtils.pathRegularPolygon(0, 0, satelliteRadius, 0, 7);
        }
    }

    private void notifySatelliteUnselected(GpsSatellite satellite) {
        if (mSatelliteSelectedListener != null) {
            mSatelliteSelectedListener.onSatelliteUnSelected(satellite);
        }
    }

    private void notifySatelliteSelected(GpsSatellite satellite) {
        if (mSatelliteSelectedListener != null) {
            mSatelliteSelectedListener.onSatelliteSelected(satellite);
        }
    }


    @Override
    protected int getSuggestedMinimumWidth() {
        return Math.max(super.getSuggestedMinimumWidth(),
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        DEFAULT_SIZE, getResources().getDisplayMetrics())));
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return getSuggestedMinimumWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getSuggestedMinimumWidth();
        }

        if (forceSquare) {
            height = width;
        } else {
            //Measure Height
            if (heightMode == MeasureSpec.EXACTLY) {
                height = heightSize;
            } else {
                height = width;
            }
        }
        setMeasuredDimension(width, height);

        centerX = width / 2;
        centerY = height / 2;
        compassRadius = Math.min(width - getPaddingLeft() - getPaddingRight(),
                height - getPaddingTop() - getPaddingBottom()) / 2;
        satelliteRadius = compassRadius / 20 / 2;
        mSatellitePaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                satelliteRadius, getResources().getDisplayMetrics()));

        initPath();
    }

    @Deprecated
    public void setSatellites(Iterable<GpsSatellite> satellites) {
        for (GpsSatellite satellite : satellites) {
            int idx = findSatelliteWrapperIndex(satellite);
            SatelliteWrapper wrapper = idx == -1 ?
                    obtainWrapper() :
                    mSatellites.get(idx);
            wrapper.setup(satellite);
            wrapper.calculate();
            tempWrapper.add(wrapper);
            if (idx >= 0) mSatellites.remove(idx);
        }
        for (SatelliteWrapper wrapper : mSatellites) {
            wrapper.recycle();
        }
        mSatellites.clear();
        ArrayList<SatelliteWrapper> temp = tempWrapper;
        tempWrapper = mSatellites;
        mSatellites = temp;
        invalidate();
    }

    private int findSatelliteWrapperIndex(GpsSatellite satellite) {
        for (int i = 0; i < mSatellites.size(); i++) {
            SatelliteWrapper wrapper = mSatellites.get(i);
            if (wrapper.wrapThis(satellite)) {
                return i;
            }
        }
        return -1;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void setSatellites(GnssStatus status) {
        mGnssStatus = status;
        if (status == null) {
            return;
        }
        for (int i = 0; i < status.getSatelliteCount(); i++) {
            int idx = findSatelliteWrapperIndex(status, i);
//            Log.d(TAG, i + " find " + idx);
            SatelliteWrapper wrapper = idx == -1 ?
                    obtainWrapper() :
                    mSatellites.get(idx);
            wrapper.setup(status, i);
            wrapper.calculate();
            tempWrapper.add(wrapper);
            if (idx >= 0) mSatellites.remove(idx);
        }


        for (SatelliteWrapper wrapper : mSatellites) {
            wrapper.recycle();
        }
        mSatellites.clear();
        ArrayList<SatelliteWrapper> temp = tempWrapper;
        tempWrapper = mSatellites;
        mSatellites = temp;
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int findSatelliteWrapperIndex(GnssStatus status, int index) {
        for (int i = 0; i < mSatellites.size(); i++) {
            SatelliteWrapper wrapper = mSatellites.get(i);
            if (wrapper.wrapThis(status, index)) {
                return i;
            }
        }
        return -1;
    }


    private void drawCompass(Canvas canvas) {
        float cx = centerX;
        float cy = centerY;
        float r = compassRadius;
        canvas.drawCircle(cx, cy, r, mCompassPaint);
        //draw auxiliary
        for (int i = 1; i <= 4; i++) {
            float _r = r * i / 4;
            canvas.drawCircle(cx, cy, _r, mAuxiliaryPaint);
        }
        canvas.drawLine(cx, cy - r, cx, cy + r, mAuxiliaryPaint);
        canvas.drawLine(cx - r, cy, cx + r, cy, mAuxiliaryPaint);
    }

    private void drawSatellites(Canvas canvas, float cx, float cy) {
        canvas.save();
        canvas.translate(cx, cy);
        if (mSatellites != null) {
            for (SatelliteWrapper wrapper : mSatellites) {
                wrapper.draw(canvas, satelliteRadius);
            }
        }
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCompass(canvas);
        drawSatellites(canvas, centerX, centerY);
    }


    private final static float[] temp = new float[]{0, 80, 1};

    public static int getSatelliteColor(float snr) {
        temp[0] = Math.min(snr / 60.f * 120, 120);
        return Color.HSVToColor(0xAA, temp);
    }

    private static final Pools.SimplePool<SatelliteWrapper> sPool = new Pools.SimplePool<>(20);

    SatelliteWrapper obtainWrapper() {
        SatelliteWrapper wrapper = sPool.acquire();
        return (wrapper != null) ? wrapper : new SatelliteWrapper();
    }

    public boolean isForceSquare() {
        return forceSquare;
    }

    public void setForceSquare(boolean forceSquare) {
        this.forceSquare = forceSquare;
    }

    public boolean isShowPrn() {
        return showPrn;
    }

    public void setShowPrn(boolean showPrn) {
        this.showPrn = showPrn;
    }

    public boolean isShowSnr() {
        return showSnr;
    }

    public void setShowSnr(boolean showSnr) {
        this.showSnr = showSnr;
    }

    public OnSatelliteSelectedListener getSatelliteSelectedListener() {
        return mSatelliteSelectedListener;
    }

    public void setSatelliteSelectedListener(OnSatelliteSelectedListener mSatelliteSelectedListener) {
        this.mSatelliteSelectedListener = mSatelliteSelectedListener;
    }

    public boolean isSatelliteSelectable() {
        return mSatelliteSelectable;
    }

    public void setSatelliteSelectable(boolean mSatelliteSelectable) {
        this.mSatelliteSelectable = mSatelliteSelectable;
    }


    private class SatelliteWrapper {

        String label;
        Rect rect = new Rect();
        private int satIndex;

        @Deprecated
        private GpsSatellite satellite;

        SatelliteWrapper() {
            satIndex = -1; // -1 未启用 gnss 系统
        }

        void recycle() {
            satellite = null;
            satIndex = -1;
            selected = false;
            label = "";
            prn = -1;
            sPool.release(this);

        }

        float x;
        float y;
        boolean selected;
        int prn;


        @TargetApi(Build.VERSION_CODES.N)
        void setup(GnssStatus status, int satIndex) {
            this.satIndex = satIndex;
            prn = GnssUtils.svidToPrn(status.getSvid(satIndex), status.getConstellationType(satIndex));
        }

        void setup(GpsSatellite satellite) {
            this.satellite = satellite;
            prn = satellite.getPrn();
        }

        boolean wrapThis(GpsSatellite satellite) {
            return satellite != null && prn == satellite.getPrn();
        }

        boolean wrapThis(GnssStatus status, int satIndex) {
            //Log.d(TAG, "test" + prn + " " + (GnssUtils.svidToPrn(status.getSvid(satIndex), status.getConstellationType(satIndex))));
            return isUsedGnss() && prn == GnssUtils.svidToPrn(status.getSvid(satIndex), status.getConstellationType(satIndex));
        }

        void calculate() {
            double radians = Math.toRadians(getAzimuth());
            float p = compassRadius / 90.0f * (90 - getElevation());
            x = (float) (p * Math.cos(radians));
            y = (float) (p * Math.sin(radians));
            label = "";
            if (showPrn || showSnr) {
                label = showPrn ?
                        Integer.toString(getPrn())
                                + (showSnr ? String.format(Locale.getDefault(), ".0%f", getSnr()) : "")
                        : String.format(Locale.getDefault(), ".0%f", getSnr()); // showSnr 在这里必定为 true
                mSatellitePaint.getTextBounds(label, 0, label.length(), rect);
            }
            if (hasLabel()) {
                rect.offset((int) (x - rect.width() / 2),
                        (int) (y + rect.height() + satelliteRadius + rect.height() / 3));
            }
            rect.union((int) (x - satelliteRadius), (int) (y - satelliteRadius), (int) (x + satelliteRadius), (int) (y + satelliteRadius));
        }

        private int getPrn() {
            return prn;
        }

        private float getElevation() {
            if (isUsedGnss()) {
                return mGnssStatus.getElevationDegrees(satIndex);
            } else {
                return satellite.getElevation();
            }
        }

        private float getAzimuth() {
            if (isUsedGnss()) {
                return mGnssStatus.getAzimuthDegrees(satIndex);
            } else {
                return satellite.getAzimuth();
            }
        }

        private float getSnr() {
            if (isUsedGnss()) {
                return mGnssStatus.getCn0DbHz(satIndex);
            } else {
                return satellite.getSnr();
            }

        }

        private boolean isUsedInFix() {
            if (isUsedGnss()) {
                return mGnssStatus.usedInFix(satIndex);
            } else {
                return satellite.usedInFix();
            }
        }

        private int getType() {
            if (isUsedGnss()) {
                return mGnssStatus.getConstellationType(satIndex);
            } else {
                return 1; // GnssStatus#CONSTELLATION_GPS
            }
        }

        /**
         * 以 View 中心点为原点
         *
         * @param canvas
         * @param radius
         */
        void draw(Canvas canvas, float radius) {
            mSatellitePaint.setStyle(Paint.Style.FILL);
            if (selected) {
                mSatellitePaint.setColor(0x88000000);
                canvas.drawCircle(x, y, radius * 1.2f, mSatellitePaint);
            }
            mSatellitePaint.setColor(getSatelliteColor(getSnr()));
//            canvas.drawCircle(x, y, radius, mSatellitePaint);
            canvas.save();
            canvas.translate(x, y);
            canvas.drawPath(constellationPaths[getType()], mSatellitePaint);
            canvas.restore();
            if (isUsedInFix()) {
                mSatellitePaint.setColor(Color.WHITE);
                canvas.drawCircle(x, y, radius / 3, mSatellitePaint);
            }

            mSatellitePaint.setStyle(Paint.Style.STROKE);
            mSatellitePaint.setColor(mSatelliteColor);
            canvas.drawText(label, rect.left, rect.bottom, mSatellitePaint);
        }


        boolean isUsedGnss() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && satIndex >= 0;
        }

        boolean contains(float x, float y) {
            return rect.contains((int) x, (int) y);
        }

        boolean hasLabel() {
            return label != null && label.length() > 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SatelliteWrapper) {
                return ((SatelliteWrapper) obj).getPrn() == getPrn();
            } else {
                return false;
            }
        }
    }

}
