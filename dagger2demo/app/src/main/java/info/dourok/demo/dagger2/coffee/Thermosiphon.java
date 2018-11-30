package info.dourok.demo.dagger2.coffee;

import javax.inject.Inject;

/**
 * Created by tiaolins on 2017/10/26.
 */

public class Thermosiphon implements Pump {
  private final Heater heater;

  @Inject
  Thermosiphon(Heater heater) {
    this.heater = heater;
  }

  @Override public void pump() {
    if (heater.isHot()) {
      System.out.println("=> => pumping => =>");
    }
  }
}
