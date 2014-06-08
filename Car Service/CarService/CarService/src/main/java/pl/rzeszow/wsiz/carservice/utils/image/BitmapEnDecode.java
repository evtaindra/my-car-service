package pl.rzeszow.wsiz.carservice.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Klasa do kodowania dekodowania obrazka do postaci bajtow
 */
public class BitmapEnDecode {
    /**
     * Kodowania obrazka do postacia bajtow
     * @param bitmap obrazek do przekodowania
     * @return ciąg bajtow obrazka
     */
    public static String BitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            String s = Base64.encodeToString(imageInByte, Base64.DEFAULT);
            return s;
        }else
            return null;
    }

    /**
     * Dekodowanie obrazka z ciągu bajtow
     * @param string ciąg bajtow obrazka
     * @return dekodowany obrazek
     */
    public static Bitmap StringToBitmap(String string) {
        if (string==null || string.equals("")){
            return  null;
        }else{
            byte[] imageAsBytes = Base64.decode(string,Base64.DEFAULT);
            Bitmap i = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            return i;
        }
    }
}
