package info.dourok.android.demo.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaTest {
  private static String strs = "static";

  /**
   * 0非捕获
   */
  public void stateless() {
    Runnable r = (() -> System.out.println("pure"));
    r.run();
  }

  /**
   * 1捕获局部变量
   */
  public void capturingLocal(String strp) {
    String str = "lexical";
    Runnable r = () -> System.out.println(str + strp);
    r.run();
  }

  /**
   * 2捕获静态变量
   */
  public void capturingStatic() {
    Runnable r = () -> System.out.println(strs);
    r.run();
  }

  /**
   * 3 捕获 super
   */
  public void testSuperScope() {
    Runnable r = () -> System.out.println(super.toString());
    r.run();
  }

  /**
   * 4 捕获 this
   */
  private String stri = "instance";

  public void capturingInstance() {
    //Runnable r = () -> System.out.println(stri);
    //r.run();
    Predicate<String> c = s -> stri.equals(s);
  }
  //方法引用

  /**
   * 5 静态方法
   */
  public void testStaticMethod() {
    Function<Integer, String> c = String::valueOf;
    System.out.println(c.apply(1));
  }

  /**
   * 6 非绑定实例方法
   */
  public void testNotBoundInstanceMethod() {
    Function<Integer, String> c = Integer::toHexString;
    System.out.println(c.apply(1));
  }

  /**
   * 7 绑定实例方法
   */
  public void testBoundInstanceMethod() {
    Predicate<String> c = (stri.equals("abc") ? "abc" : "bcd")::equals;
    System.out.println(c.test("abc"));
  }

  /**
   * 8 捕获实例方法
   */
  public void capturingIntanceMethod() {
    Predicate<String> c = stri::equals;
    System.out.println(c.test("abc"));
  }

  /**
   * 9 捕获 super
   */
  public void testCaptureSuperMethod() {
    Supplier<String> s = super::toString;
    System.out.println(s.get());
  }

  /**
   * 10 捕获 this
   */
  public void testCaptureThisMethod() {
    Supplier<String> s = this::toString;
    System.out.println(s.get());
  }

  /**
   * 11 捕获构造函数
   */
  public void testNewMethod() {
    Supplier<Object> s = Object::new;
    System.out.println(s.get());
  }

  /**
   * 12 捕获内部类构造函数
   */
  public void testInnerNewMethod() {
    Supplier<Inner> s = Inner::new;
    System.out.println(s.get());
  }

  /**
   * 不定参数方法引用
   */
  public void testVAragMethod() {
    Function<String, List<String>> f = Arrays::asList;
    System.out.println(f.apply("wtf"));
  }

  private class Inner {

  }

  public static void main(String[] args) {
    LambdaTest test = new LambdaTest();
    test.stateless();
    test.capturingLocal("test");
    test.capturingStatic();
    test.testSuperScope();
    test.capturingInstance();

    test.testStaticMethod();
    test.testNotBoundInstanceMethod();
    test.testBoundInstanceMethod();
    test.capturingIntanceMethod(); //9
    test.testCaptureSuperMethod(); //10  //需要生成桥接方法
    test.testCaptureThisMethod(); //11
    test.testNewMethod();
    test.testInnerNewMethod(); // //需要生成桥接方法
    test.testVAragMethod();//14 //需要生成桥接方法
  }
}

