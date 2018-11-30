package info.dourok.demo.dagger2;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by tiaolins on 2017/10/26.
 */

@Module(subcomponents = {
    MainSubcomponent.class
}) abstract class DeclarationsModule {
  @Binds @IntoMap @ActivityKey(MainActivity.class)
  abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(
      MainSubcomponent.Builder builder);
}
