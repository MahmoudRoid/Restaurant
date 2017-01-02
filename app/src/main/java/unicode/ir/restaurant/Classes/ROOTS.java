package unicode.ir.restaurant.Classes;

import android.os.Environment;

/**
 * Created by mohad on 11/30/2016.
 */

public class ROOTS {
    public static final String APP_NAME   ="/DOCOTR";
    public static final String ROOT_DIR     = Environment.getExternalStorageDirectory()+APP_NAME;


    public static final String VIDEOS_SAVE   =APP_NAME+"/VIDEOS_SAVE";
    public static final String VIDEOS   = ROOT_DIR + "/VIDEOS";
    public static final String IMAGES   = ROOT_DIR + "/IMAGES";
    public static final String VOICES   = ROOT_DIR + "/VOICES";
    public static final String PDFS     = ROOT_DIR + "/PDFS";


}// end class
