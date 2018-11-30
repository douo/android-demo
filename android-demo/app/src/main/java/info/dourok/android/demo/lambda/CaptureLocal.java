package info.dourok.android.demo.lambda;

import java.util.Arrays;

/**
 * Created by tiaolins on 2017/10/17.
 */

public class CaptureLocal {
  public static void main(String[] args) {
    Runnable r = () -> System.out.println(args.length);
    r.run();
  }
}
