package info.dourok.android.demo.coordinate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.lang.annotation.Annotation;

import info.dourok.android.demo.R;
import info.dourok.android.demo.annotation.AnnotationDemo;


@AnnotationDemo(CoordinatorDemoActivity.class)
public class CoordinatorDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_demo);
        int i = getResources().getIdentifier("colorControlActivated", "attr", getPackageName());
        int j = android.support.v7.appcompat.R.attr.colorControlActivated;
        System.out.println(i + ":" + j);

        System.out.println("length:" + getClass().getAnnotations().length);
        for (Annotation annotation : this.getClass().getAnnotations()) {
            System.out.println(annotation.toString());
        }
    }


    public static class MyBehavior extends CoordinatorLayout.Behavior<TextView> {
        public MyBehavior() {
            super();
            System.out.println("MyBehavior");
        }

        public MyBehavior(Context context, AttributeSet as) {
            super(context, as);
            System.out.println("MyBehavior:" + as);
        }

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, TextView child, MotionEvent ev) {
            System.out.println("onInterceptTouchEvent");
            return super.onInterceptTouchEvent(parent, child, ev);
        }

        @Override
        public boolean onTouchEvent(CoordinatorLayout parent, TextView child, MotionEvent ev) {
            System.out.println("onTouchEvent");
            return super.onTouchEvent(parent, child, ev);
        }

        @Override
        public boolean blocksInteractionBelow(CoordinatorLayout parent, TextView child) {
            return super.blocksInteractionBelow(parent, child);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
            return dependency instanceof NestedScrollView;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
            return super.onDependentViewChanged(parent, child, dependency);
        }

        @Override
        public void onDependentViewRemoved(CoordinatorLayout parent, TextView child, View dependency) {
            super.onDependentViewRemoved(parent, child, dependency);
        }


        @Override
        public boolean onMeasureChild(CoordinatorLayout parent, TextView child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, TextView child, int layoutDirection) {
            return super.onLayoutChild(parent, child, layoutDirection);
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, TextView child, View directTargetChild, View target, int nestedScrollAxes) {
            System.out.println("onStartNestedScroll");
            return true;
        }

        @Override
        public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, TextView child, View directTargetChild, View target, int nestedScrollAxes) {
            super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        }

        @Override
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, TextView child, View target) {
            super.onStopNestedScroll(coordinatorLayout, child, target);
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, TextView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            System.out.println("onNestedScroll:" + dxConsumed + ":" + dyConsumed + "," + dxUnconsumed + ":" + dyUnconsumed);
            child.setTranslationX(dyConsumed);
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, TextView child, View target, int dx, int dy, int[] consumed) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        }

        @Override
        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, TextView child, View target, float velocityX, float velocityY, boolean consumed) {
            return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        }

        @Override
        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, TextView child, View target, float velocityX, float velocityY) {
            return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        }

        @Override
        public WindowInsetsCompat onApplyWindowInsets(CoordinatorLayout coordinatorLayout, TextView child, WindowInsetsCompat insets) {
            return super.onApplyWindowInsets(coordinatorLayout, child, insets);
        }

        @Override
        public void onRestoreInstanceState(CoordinatorLayout parent, TextView child, Parcelable state) {
            super.onRestoreInstanceState(parent, child, state);
        }

        @Override
        public Parcelable onSaveInstanceState(CoordinatorLayout parent, TextView child) {
            return super.onSaveInstanceState(parent, child);
        }
    }
}
