package info.dourok.demo.dagger2;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Created by tiaolins on 2017/10/26.
 */

@Component(modules = {
    DeclarationsModule.class,
    AndroidInjectionModule.class
})
public interface AppComponent extends AndroidInjector<MyApplication> {
  // 继承  AndroidInjector.Builder
  // 才会生成一个工厂方法将 MyApplication 实例传入对象图中
  @Component.Builder
  abstract class Builder extends AndroidInjector.Builder<MyApplication> {

  }
}
