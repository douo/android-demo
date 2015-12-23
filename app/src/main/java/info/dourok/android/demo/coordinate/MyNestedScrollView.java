package info.dourok.android.demo.coordinate;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

/**
 * Created by John on 2015/10/17.
 */
public class MyNestedScrollView extends View implements NestedScrollingChild {
    NestedScrollingChildHelper mChildHelper;

    public MyNestedScrollView(Context context) {
        this(context, null);
    }

    public MyNestedScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyNestedScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        boolean b = mChildHelper.isNestedScrollingEnabled();
        d("isNestedScrollingEnabled:" + mChildHelper);
        return b;
    }

    private void d(String s) {
        Log.d("MyNestedScrollView", s);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        d("startNestedScroll:" + axes);
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        d("stopNestedScroll");
        mChildHelper.stopNestedScroll();

    }

    @Override
    public boolean hasNestedScrollingParent() {
        boolean b = mChildHelper.hasNestedScrollingParent();
        d("hasNestedScrollingParent:" + b);
        return b;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        d("dispatchNestedScroll:" + dxConsumed + "," + dyConsumed + "," + dxUnconsumed + "," + dyUnconsumed);
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        d("dispatchNestedPreScroll:" + dx + "," + dy);
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        d("dispatchNestedFling:" + velocityX + "," + velocityY);
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        d("dispatchNestedPreFling:" + velocityX + "," + velocityY);
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
