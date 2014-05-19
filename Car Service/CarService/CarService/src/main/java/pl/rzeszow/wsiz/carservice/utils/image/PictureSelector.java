package pl.rzeszow.wsiz.carservice.utils.image;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Pair;

import java.io.File;

import pl.rzeszow.wsiz.carservice.R;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class PictureSelector {

    private Context mContext;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;

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

    public Pair<Bitmap,String> onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    bm = Bitmap.createScaledBitmap(bm, 250, 250, true);
                    f.delete();
                    return new Pair<Bitmap, String>(bm, "photo");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = mContext.getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int pathColumnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(pathColumnIndex);
                cursor.close();

                Uri filePathUri;
                filePathUri = Uri.parse(filePath);
                String fileName = filePathUri.getLastPathSegment();

                Bitmap bm;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(filePath, bitmapOptions);

                bm = Bitmap.createScaledBitmap(bm, 250, 250, true);

                return new Pair<Bitmap, String>(bm,fileName);
            }
        }
        return null;
    }
}
