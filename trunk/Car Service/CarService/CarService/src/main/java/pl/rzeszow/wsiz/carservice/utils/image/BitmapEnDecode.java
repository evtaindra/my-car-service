package pl.rzeszow.wsiz.carservice.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by rsavk_000 on 5/2/2014.
 */
public class BitmapEnDecode {
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
