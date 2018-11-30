package info.dourok.demo.dagger2;

import android.app.Application;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.DaggerAppCompatActivity;
import javax.inject.Inject;

/**
 * Created by tiaolins on 2017/10/26.
 */

public class MyApplication extends DaggerApplication {

  @Override protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
    return DaggerAppComponent.builder().create(this);
  }
}
