package info.dourok.demo.dagger2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dagger.android.AndroidInjection
import info.dourok.demo.dagger2.coffee.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
  //@Inject var a: CoffeeMaker? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val c = CoffeeMaker()
    val c1 = CoffeeMaker()
    DaggerCoffeeShop.create().fill(c)
    DaggerCoffeeShop.create().fill(c1)
    DaggerOtherComponent.builder().build()
    DaggerCoffeeShop.builder().otherComponent()
    println("finish")
  }
}
