package info.dourok.android.demo.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by tiaolins on 2017/4/11.
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public class CustomTransition extends Transition {

    private static final String TAG = "CustomTransition";

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        View v = transitionValues.view;
        Log.d(TAG, "captureStartValues:" + v + (v instanceof TextView ? "\n text:" + ((TextView) v).getText().toString() : ""));
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        View v = transitionValues.view;
        Log.d(TAG, "captureEndValues:" + v + (v instanceof TextView ? "\n text:" + ((TextView) v).getText().toString() : ""));
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        return super.createAnimator(sceneRoot, startValues, endValues);
    }
}
