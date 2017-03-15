package info.dourok.android.demo.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Created by tiaolins on 2017/2/27.
 */

public class NetworkUtils {

    private static final String TEST_HOST = "www.baidu.com";
    private static final String TAG = "NetworkUtils";

    private static final Handler sHandler;
    static {
        if(Looper.myLooper() == Looper.getMainLooper()){
            sHandler = new Handler();
        }else{
            sHandler = new Handler(Looper.getMainLooper());
        }
    }
    /**
     * 通过  {@link InetAddress} 测试网络是否可用
     * consumer 将在主线程执行
     * @param consumer
     */
    public static void isInternetAvailable(final Consumer<Boolean> consumer) {
        new Thread() {
            @Override
            public void run() {
                try {
                    final InetAddress ipAddr = InetAddress.getByName(TEST_HOST); //You can replace it with your name
                    sendResult(!ipAddr.equals(""), sHandler, consumer);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendResult(false, sHandler, consumer);
                }
            }
        }.start();
    }

    private static <T> void sendResult(final T t, Handler handler, final Consumer<T> consumer) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                consumer.accept(t);
            }
        });
    }


    /**
     * 通过 ping 测试网络是否可用
     * consumer 将在主线程执行
     * @param consumer
     */
    public static void isInternetAvailable2(final Consumer<Boolean> consumer) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String ip = TEST_HOST;//ping的地址，可以换做任何可靠的网站
                    Process process = Runtime.getRuntime().exec("ping -c 3 -w 5 " + ip);//ping 网址3次
                    //读区ping 的内容
                    InputStream inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    //ping 的状态
                    int status = process.waitFor();
                    Log.d(TAG, "status: " + status);
                    if (status == 0) {
                        sendResult(true, sHandler, consumer);
                    } else {
                        sendResult(false, sHandler, consumer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendResult(false, sHandler, consumer);
                }
            }
        }.start();
    }
}
