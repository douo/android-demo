package info.dourok.camera;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;

/**
 * Created by larry on 6/13/16.
 */
public abstract class RotationEventListener {
    private final static int THRESHOLD = 60;
    private OrientationEventListener mOrientationEventListener;
    private int currentRotation;

    private boolean mIgnore180;


    public RotationEventListener(Context context) {
        this(context, false);
    }

    /**
     * @param context
     * @param ignore180 是否忽略 180 屏幕方向，即倒转的竖屏
     */
    public RotationEventListener(Context context, boolean ignore180) {
        if (context instanceof Activity) {
            currentRotation = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();
        }
        mIgnore180 = ignore180;
        mOrientationEventListener = new OrientationEventListener(context) {
            @Override
            public void onOrientationChanged(int orientation) {
                int cDegrees = getDegrees(currentRotation);
                //转换成顺时钟方向
                int degrees = (360 - orientation) % 360; // orientation 可能为负，出现过 -1
                d(cDegrees + ":" + degrees + " - " + orientation);
                if (degrees > (cDegrees + THRESHOLD) % 360 || degrees < (cDegrees + 360 - THRESHOLD) % 360) { //变化超过 60 度触发更改方向。
                    int rotation = mIgnore180 ? getRotationIgnore180(degrees) : getRotation(degrees);
                    d("rotation:" + rotation);
                    if (rotation != currentRotation) { // 忽略 180 的情况下，可能触发后 rotation 仍然相同
                        d("onOrientationChanged:" + orientation + " " + rotation);
                        onRotationChanged(rotation, currentRotation);
                        currentRotation = rotation;
                    }
                }

            }
        };

    }

    public static int getDegrees(int rotation) {
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        return degrees;
    }

    public static int getRotationIgnore180(int degrees) {
        if (degrees > 315 || degrees <= 45) {
            return Surface.ROTATION_0;
        } else if (degrees > 45 && degrees <= 180) {
            return Surface.ROTATION_90;
        } else {
            return Surface.ROTATION_270;
        }
    }

    public static int getRotation(int degrees) {
        if (degrees > 315 || degrees <= 45) {
            return Surface.ROTATION_0;
        } else if (degrees > 45 && degrees <= 135) {
            return Surface.ROTATION_90;
        } else if (degrees > 135 && degrees <= 225) {
            return Surface.ROTATION_180;
        } else {
            return Surface.ROTATION_270;
        }
    }

    public boolean isIgnore180() {
        return mIgnore180;
    }

    public void enable() {
        mOrientationEventListener.enable();
    }

    public void disable() {
        mOrientationEventListener.disable();
    }

    protected void d(String s) {
        Log.d("RotationEventListener", s);
    }

    protected abstract void onRotationChanged(int newRotation, int oldRotation);

    /**
     * rotation 为屏幕方向
     * 与 {@link Display#getRotation()} 一致
     * 可能值为 {@link Surface#ROTATION_0 Surface.ROTATION_0}
     * (no rotation), {@link Surface#ROTATION_90 Surface.ROTATION_90},
     * {@link Surface#ROTATION_180 Surface.ROTATION_180},
     * {@link Surface#ROTATION_270 Surface.ROTATION_270}.
     *
     * @return 当前屏幕方向 Surface.Rotation
     */

    public int getRotation() {
        return currentRotation;
    }
}
