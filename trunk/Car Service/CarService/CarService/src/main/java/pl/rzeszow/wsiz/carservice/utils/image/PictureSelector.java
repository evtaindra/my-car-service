package pl.rzeszow.wsiz.carservice.utils.image;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

import pl.rzeszow.wsiz.carservice.R;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class PictureSelector {

    private Context mContext;
    public static int REQUEST_CAMERA = 0;
    public static int SELECT_FILE = 1;

    public PictureSelector(Context context){
        mContext = context;
    }

    public AlertDialog buildImageDialog() {
        final CharSequence[] items = mContext.getResources().getStringArray(R.array.image_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Intent intent = null;
                switch (item){
                    case 0:
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment
                                .getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult((Activity) mContext,intent, REQUEST_CAMERA, null);
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult((Activity)mContext,
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE,null);
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        return builder.create();
    }
}
