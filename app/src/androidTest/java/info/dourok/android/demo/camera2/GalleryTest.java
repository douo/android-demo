package info.dourok.android.demo.camera2;

import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by larry on 6/16/16.
 */
public class GalleryTest extends AndroidTestCase {

    private Gallery mGallery;

    @Before
    public void setUp() throws Exception {
        mGallery = new Gallery(getContext(), "demo");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testStripExtension() throws Exception {
        assertNotNull(getContext());
    }

    @Test
    public void testToJson() throws Exception {

    }

    @Test
    public void testFromJson() throws Exception {

    }

    @Test
    public void testGetPictures() throws Exception {

    }

    @Test
    public void testDeletePicture() throws Exception {

    }

    @Test
    public void testUpdatePicture() throws Exception {

    }

    @Test
    public void testGetImageWorker() throws Exception {

    }
}