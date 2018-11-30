package info.dourok.android.demo.camera2;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larry on 6/11/16.
 */
public class Gallery {
    public static final String KEY_PICTURES = "KEY_PICTURES";
    public static final String EXT = ".jpg";
    public static final int THUMBNAIL_SIZE = 256;
    public static final String THUMB_EXT = ".thumb";
    String id;
    Context context;
    String parentDir;
    List<Picture> mPictures;
    private File mFile;

    public Gallery(Context context, String id) {
        this(context, id, context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
    }

    public Gallery(Context context, String id, String parentDir) {
        this.id = id;
        this.context = context;
        this.parentDir = parentDir;
        mFile = new File(parentDir, id);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
    }

    static String stripExtension(String str) {
        // Handle null case specially.
        if (str == null) return null;
        // Get position of last '.'.
        int pos = str.lastIndexOf(".");
        // If there wasn't any '.' just return the string as is.
        if (pos == -1) return str;
        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }

    public static String toJson(List<Picture> pictures) {
        return new Gson().toJson(pictures);
    }

    public static List<Picture> fromJson(String data) {
        return new Gson().fromJson(data, new TypeToken<List<Picture>>() {
        }.getType());
    }

    public List<Picture> getPictures() {
        if (mPictures == null) {
            mPictures = buildPictureList();
        }
        return mPictures;
    }

    public void deletePicture(Picture picture) {
        picture.delete();
        mPictures.remove(picture);
    }

    public void updatePicture(List<Picture> pictures) {
        mPictures.clear();
        mPictures.addAll(pictures);
    }

    private List<Picture> buildPictureList() {
        List<Picture> pictures = new ArrayList<>(10);
        for (File f : getPictureDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(EXT);
            }
        })) {
            pictures.add(buildPicture(f));
        }
        return pictures;
    }

    File getPictureDir() {

        return mFile;
    }

    Picture buildPicture(File file) {
        long size = file.length();
        int timestamp = (int) (Long.parseLong(stripExtension(file.getName())) / 1000);
        return new Picture(size, timestamp, Uri.fromFile(file).toString(),
                Uri.fromFile(new File(stripExtension(file.getAbsolutePath()) + THUMB_EXT)).toString());
    }

    String[] newImageName() {
        String basename = Long.toString(System.currentTimeMillis());
        return new String[]{basename + EXT, basename + THUMB_EXT};
    }

}
