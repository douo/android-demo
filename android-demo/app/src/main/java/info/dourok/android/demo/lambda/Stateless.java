package info.dourok.android.demo.lambda;

import android.annotation.TargetApi;
import android.os.Build;
import java.util.function.Function;

/**
 * Created by tiaolins on 2017/10/17.
 */

public class Stateless {
  @TargetApi(Build.VERSION_CODES.N) public static void main(String[] args) {
    Function<String, Integer> f = s -> Integer.valueOf(s);
    f.apply("123");

    ParameterInterface fi = Stateless::comp;

    // lambda 不支持有类型参数的方法
    // ParameterInterface f2 = (t1,t2) -> t1.compareTo(t2);


  }



  public static <T extends Comparable<T>> int comp(T t1, T t2) {
    return 0;
  }
}
