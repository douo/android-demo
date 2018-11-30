package info.dourok.demo.dagger2.coffee;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = DripCoffeeModule.class, dependencies = OtherComponent.class)
public interface CoffeeShop {
  CoffeeMaker maker();

  public CoffeeMaker fill(CoffeeMaker maker);

  Heater heater();
}