package info.dourok.android.demo.oreo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import info.dourok.android.demo.services.ServiceDemo;

public class OreoReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    d(intent.getAction());
    Intent i = new Intent(context, ServiceDemo.class);
    context.startForegroundService(i);

  }
  public void d(Object o) {
    Log.d("OreoReceiver", o == null ? "null" : o.toString());
  }
}
