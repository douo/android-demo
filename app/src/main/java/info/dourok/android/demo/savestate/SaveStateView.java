package info.dourok.android.demo.savestate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import static info.dourok.android.demo.LogUtils.LOGD;

/**
 * Created by John on 2016/2/18.
 */
public class SaveStateView extends View {
    private static final String TAG = "SaveStateView";
    int rand;
    Paint textPaint;
    private int hash = System.identityHashCode(this);

    public SaveStateView(Context context) {
        this(context, null, 0);
    }

    public SaveStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SaveStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rand = new Random().nextInt(0x1000000);
        textPaint = new Paint();
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setStrokeWidth(3);
        textPaint.setTextSize(24f);
        textPaint.getTextBounds("WWWWWWWWWW", 0, 10, rect);
        rand |= 0xFF000000;
    }

    void updateRand(int r) {
        rand = r;
        textPaint.setColor(0x1000000 - (rand & 0x00FFFFFF) | 0xFF000000);
        invalidate();
    }

    Rect rect = new Rect();

    @Override
    public void setSaveEnabled(boolean enabled) {
        super.setSaveEnabled(enabled);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        d("onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return rect.width();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return rect.height();
    }

    /**
     * 需要有id才能被保存
     *
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        d("onSaveInstanceState");
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.rand = rand;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        d("onRestoreInstanceState:" + state);
        SavedState ss = (SavedState) state;
        //注意这里是要调用 getSuperState
        super.onRestoreInstanceState(ss.getSuperState());
        updateRand(ss.rand);
    }

    public static class SavedState extends BaseSavedState {
        int rand;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            rand = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(rand);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(rand);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        canvas.drawText(String.format("0x%X", rand), 0, rect.height(), textPaint);
    }

    protected void d(String msg) {
        LOGD(String.format("%s:0x%X", TAG, hash), msg);
    }
}
