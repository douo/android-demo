package info.dourok.demo.dagger2.coffee;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module class DripCoffeeModule {
  @Singleton @Provides static Heater provideHeater() {
    return new ElectricHeater();
  }

  @Provides static Pump providePump(Thermosiphon pump) {
    return pump;
  }
}