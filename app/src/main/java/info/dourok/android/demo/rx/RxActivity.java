package info.dourok.android.demo.rx;

import android.app.Activity;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import info.dourok.android.demo.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;

public class RxActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Excp", "defualt:" + Thread.getDefaultUncaughtExceptionHandler());
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.d("Excp", "Current:" + Thread.currentThread().toString());
                Log.d("Excp", thread.toString());
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        });
        setContentView(R.layout.activity_rx);
        Observable.from(new String[]{"A", "B", "C"})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(Thread.currentThread());
                        System.out.println(s);
                    }
                }).unsubscribe();

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(3);
                subscriber.onNext(2);
                subscriber.onCompleted();
            }
        }).lift(new Observable.Operator<String, Integer>() {
            @Override
            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {

                return new ActionSubscriber<>(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        subscriber.onNext(integer.toString());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }, new Action0() {
                    @Override
                    public void call() {

                    }
                });
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("Action:" + s);
            }
        });

    }

}
