package info.dourok.android.demo.camera2;

import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by larry on 6/18/16.
 */

@RunWith(AndroidJUnit4ClassRunner.class)
public class SimpleCameraActivityTest extends ActivityInstrumentationTestCase2<SimpleCameraActivity>{

    public SimpleCameraActivityTest(Class<SimpleCameraActivity> activityClass) {
        super(activityClass);
    }

    @Test
    public void testOnCreate() throws Exception {

    }
}