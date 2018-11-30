package info.dourok.demo.dagger2;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by tiaolins on 2017/10/26.
 */
@Subcomponent
public interface MainSubcomponent extends AndroidInjector<MainActivity> {
  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<MainActivity> {

  }
}
