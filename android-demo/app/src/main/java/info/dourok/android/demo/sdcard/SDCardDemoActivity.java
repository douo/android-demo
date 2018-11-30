package info.dourok.android.demo.sdcard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;
import android.view.View;
import info.dourok.android.demo.R;
import info.dourok.android.demo.utils.FileUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SDCardDemoActivity extends Activity {

  private static final String TAG = "SDCardDemoActivity";
  private static final int REQUEST_PICK = 0x12;
  private static final int WRITE_REQUEST_CODE = 0x43;

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sdcard_demo);

    StorageOptions.determineStorageOptions();
    Log.w(TAG, "count:" + StorageOptions.count);
    for (String label : StorageOptions.labels) {
      Log.w(TAG, "label:" + label);
    }
    for (String path : StorageOptions.paths) {
      Log.w(TAG, "path:" + path);
    }
    testExternalFiles();
    testExternalStoragePublic();
    testPortableStoragePublic();
    Log.d(TAG,
        EnvironmentCompat.getStorageState(Environment.getExternalStoragePublicDirectory("test")));
  }

  private void testExternalStoragePublic() {
    Log.d(TAG, "testExternalStoragePublic");
    File f = Environment.getExternalStoragePublicDirectory("test");
    if (!f.exists()) {
      f.delete();
      System.out.println(f.mkdir());
    }
    testFile(f);
  }

  private void testPortableStoragePublic() {
    Log.d(TAG, "testPortable");
    File f = new File("/storage/36B8-4DF8/");
    for (String name : f.list()) {
      Log.d(TAG, name);
    }
    testFile(f);
  }

  @TargetApi(Build.VERSION_CODES.KITKAT) private void testExternalFiles() {
    Log.d(TAG, "testExternalFiles");
    File[] fs = getExternalFilesDirs(null);
    for (File f : fs) {
      testFile(f);
    }
  }

  private void testFile(File f) {
    Log.d(TAG, f.getAbsolutePath() + ":" + FileUtils.testWrite(f));
  }

  public void testSaf(View v) {
    Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    i.setType("*/*");
    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    startActivityForResult(i, REQUEST_PICK);
  }

  public void testCreate(View v) {
    createFile("text/plain", "wtf.txt");
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      Uri uri = null;
      if (data != null) {
        uri = data.getData();
        Log.i(TAG, "Uri: " + uri.toString());
        dumpMetaData(uri);
        try {
          Log.d(TAG, readTextFromUri(uri));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void testUri(View view) {
    final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION |
        Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

    Uri uri =
        Uri.parse("content://com.android.externalstorage.documents/document/36B8-4DF8%3ADCIM");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      getContentResolver().takePersistableUriPermission(uri, takeFlags);
    }
    dumpMetaData(uri);
  }

  private void createFile(String mimeType, String fileName) {

    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

    // Filter to only show results that can be "opened", such as
    // a file (as opposed to a list of contacts or timezones).
    intent.addCategory(Intent.CATEGORY_OPENABLE);

    // Create a file with the requested MIME type.
    intent.setType(mimeType);
    intent.putExtra(Intent.EXTRA_TITLE, fileName);
    startActivityForResult(intent, WRITE_REQUEST_CODE);
  }

  private String readTextFromUri(Uri uri) throws IOException {
    InputStream inputStream = getContentResolver().openInputStream(uri);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder stringBuilder = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      stringBuilder.append(line);
    }
    //fileInputStream.close();
    //parcelFileDescriptor.close();
    return stringBuilder.toString();
  }

  public void dumpMetaData(Uri uri) {

    // The query, since it only applies to a single document, will only return
    // one row. There's no need to filter, sort, or select fields, since we want
    // all fields for one document.
    Cursor cursor = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
      cursor = getContentResolver().query(uri, null, null, null, null, null);
    }

    try {
      // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
      // "if there's anything to look at, look at it" conditionals.
      if (cursor != null && cursor.moveToFirst()) {

        // Note it's called "Display Name".  This is
        // provider-specific, and might not necessarily be the file name.
        for (int i = 0; i < cursor.getColumnCount(); i++) {
          Log.i(TAG, cursor.getColumnName(i) + ": " + cursor.getString(i));
        }
      }
    } finally {
      cursor.close();
    }
  }
}
