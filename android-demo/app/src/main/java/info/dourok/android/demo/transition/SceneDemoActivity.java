package info.dourok.android.demo.transition;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.transition.Transition;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import info.dourok.android.demo.R;

public class SceneDemoActivity extends AppCompatActivity {

    private static final String TAG = "SceneDemoActivity";
    private Scene scene1;
    private Scene scene2;
    private Scene currentScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_demo);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            scene1 = Scene.getSceneForLayout((ViewGroup) findViewById(R.id.scene_root), R.layout.scene_demo_1, this);
            scene2 = Scene.getSceneForLayout((ViewGroup) findViewById(R.id.scene_root), R.layout.scene_demo_2, this);
            scene1.setEnterAction(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "scene1 enter");
                    currentScene = scene1;
                }
            });
            scene2.setEnterAction(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "scene2 enter");
                    currentScene = scene2;
                }
            });
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void transition(View view) {
        TransitionSet set = new TransitionSet();
        Fade fade = new Fade();
        set.setDuration(3000);
//        fade.excludeTarget(((ViewGroup) findViewById(R.id.scene_root)).getChildAt(0), true);
        fade.excludeTarget(R.id.text_1, true);
        set.addTransition(fade).addTransition(new ChangeBounds()).addTransition(new CustomTransition()).addTransition(new ChangeTransform());
        if (currentScene == scene1) {
            TransitionManager.go(scene2, set);
        } else {
            TransitionManager.go(scene1, set);
        }

    }
}
