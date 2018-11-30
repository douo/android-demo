package info.dourok.android.demo.sqlite3;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.io.File;

import info.dourok.android.demo.R;

public class DbDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_demo);
        SQLiteOpenHelper helper = new MySqliteHelper(this,
                new File(getExternalFilesDir(null),"test.db").getAbsolutePath(),null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.close();
    }
}
