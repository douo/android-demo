package info.dourok.android.demo.lambda;

/**
 * Created by tiaolins on 2017/10/19.
 */

public interface ParameterInterface {
  <T extends Comparable<T>> int comp(T t1, T t2);
}
