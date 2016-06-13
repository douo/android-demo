package info.dourok.android.demo.camera2;

import java.io.File;

/**
 * 文件名为时间戳
 * Created by larry on 6/11/16.
 */
public class Picture {
    long size;
    int date;
    String imageUri;
    String thumbUri;

    Picture(long size, int date, String imageUri, String thumbUri) {
        this.size = size;
        this.date = date;
        this.imageUri = imageUri;
        this.thumbUri = thumbUri;
    }


    public boolean delete() {
        new File(thumbUri).delete();
        return new File(imageUri).delete();
    }
}
