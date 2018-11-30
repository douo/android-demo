package info.dourok.android.demo.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by larry on 4/20/16.
 */
public class FileUtils {
    private static final int BUFFER_SIZE = 1024 * 1000;
    private static final String TAG = "FileUtils";

    public static void writeFileContents(final File file, String content) throws IOException {
        final OutputStream outputStream = new FileOutputStream(file);
        try {
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } finally {
            outputStream.close();
        }
    }

    public static boolean deleteDir(final File dir) {
        boolean b = false;
        for (File f : dir.listFiles()) {
            if(f.isFile()) {
                b &= f.delete();
            }else{
                b &= deleteDir(f);
            }
        }
        b &= dir.delete();
        return b;
    }

    public static String getFileContents(final File file) throws IOException {

        final InputStream inputStream = new FileInputStream(file);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        final StringBuilder stringBuilder = new StringBuilder();

        boolean done = false;

        while (!done) {
            final String line = reader.readLine();
            done = (line == null);

            if (line != null) {
                stringBuilder.append(line);
            }
        }

        reader.close();
        inputStream.close();

        return stringBuilder.toString();
    }

    public static String join(String prefix, String suffix) {
        int prefixLength = prefix.length();
        boolean haveSlash = (prefixLength > 0 && prefix.charAt(prefixLength - 1) == File.separatorChar);
        if (!haveSlash) {
            haveSlash = (suffix.length() > 0 && suffix.charAt(0) == File.separatorChar);
        }
        return haveSlash ? (prefix + suffix) : (prefix + File.separatorChar + suffix);
    }

    /**
     * 测试目录是否可写
     *
     * @return
     */
    public static boolean testWrite(File file) {

        if (file.isDirectory() && file.canWrite()) {
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            File testWritable = new File(file.getAbsolutePath(), "test_" + timeStamp);
            boolean test = testWritable.mkdirs();
            if (test) {
                testWritable.delete();
                return true;
            }
        }
        return false;
    }

    //
    public static void readZip(String zipStr) throws IOException {
        File zip = new File(zipStr);
        ZipFile zf = new ZipFile(zip);
        Enumeration entryEnumeration = zf.entries();
        while (entryEnumeration.hasMoreElements()) {
            System.out.println(((ZipEntry) (entryEnumeration.nextElement())).getName());
        }
    }

    public static void zip(String[] files, String zipFile) throws IOException {
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                } finally {
                    origin.close();
                }
            }
        } finally {
            out.close();
        }
    }

    /**
     * @param is
     * @param location
     */
    public static void unzip(InputStream is, String location) throws IOException {
        int size;
        byte[] buffer = new byte[BUFFER_SIZE];
        if (!location.endsWith("/")) {
            location += "/";
        }
        File f = new File(location);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(is, BUFFER_SIZE));
        ZipEntry ze = null;
        try {
            while ((ze = zin.getNextEntry()) != null) {
                String path = location + ze.getName();
                File unzipFile = new File(path);
                Log.d(TAG, "unziping:" + unzipFile);
                if (ze.isDirectory()) {
                    if (!unzipFile.isDirectory()) {
                        unzipFile.mkdirs();
                    }
                } else {
                    // check for and create parent directories if they don't exist
                    File parentDir = unzipFile.getParentFile();
                    if (null != parentDir) {
                        if (!parentDir.isDirectory()) {
                            parentDir.mkdirs();
                        }
                    }

                    // unzip the file
                    FileOutputStream out = new FileOutputStream(unzipFile, false);
                    BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
                    try {
                        while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1) {
                            fout.write(buffer, 0, size);
                        }
                        zin.closeEntry();
                    } finally {
                        fout.flush();
                        fout.close();
                    }
                }
            }
        } finally {
            zin.close();
        }
    }

    /**
     * @param zipFile
     * @param location
     * @throws IOException
     */
    public static void unzip(String zipFile, String location) throws IOException {
        unzip(new FileInputStream(zipFile), location);
    }

    public static String stripExtension(String str) {
        // Handle null case specially.
        if (str == null) return null;
        // Get position of last '.'.
        int pos = str.lastIndexOf(".");
        // If there wasn't any '.' just return the string as is.
        if (pos == -1) return str;
        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }

    public static String getFilePathFromUri(String fileUrl) {
        return Uri.parse(fileUrl).getPath();
    }

    /**
     * 把一个文件复制到另一个文件下
     *
     * @param thumb
     * @param dst
     */
    public static void copyFile(File thumb, File dst) {
        try {
            FileInputStream fosfrom = new FileInputStream(thumb);
            FileOutputStream fosto = new FileOutputStream(dst);

            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            fosto.getFD().sync();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制源目录下所有文件到目标目录
     * /source/a -> /target/a
     *
     * @param source
     * @param target
     * @throws IOException
     */
    public static void copyDirectory(File source, File target)
            throws IOException {

        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdir();
            }

            String[] children = source.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(source, children[i]),
                        new File(target, children[i]));
            }
        } else {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target);
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
}