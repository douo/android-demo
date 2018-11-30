package info.dourok.android.demo.camera2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by larry on 6/16/16.
 */
public class GalleryLocalTest {

    @Test
    public void testStripExtension() throws Exception {
        assertEquals("wtf",Gallery.stripExtension("wtf.wtf"));
        assertEquals("wtf",Gallery.stripExtension("wtf."));
        assertEquals("wtf.wtf.we",Gallery.stripExtension("wtf.wtf.we.wet"));
        assertEquals("wtf",Gallery.stripExtension("wtf"));
        assertEquals("","",Gallery.stripExtension(""));
    }

}