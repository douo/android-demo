package info.dourok.demo.dagger2.coffee;

import javax.inject.Inject;

/**
 * Created by tiaolins on 2017/10/26.
 */

public class CoffeeMaker {
  @Inject Heater heater;
  @Inject Pump pump;

  @Inject public CoffeeMaker() {
  }
}
