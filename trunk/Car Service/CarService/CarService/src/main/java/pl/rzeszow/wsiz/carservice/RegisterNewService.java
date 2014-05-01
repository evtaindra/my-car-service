package pl.rzeszow.wsiz.carservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.image.PictureSelector;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class RegisterNewService extends Activity implements View.OnClickListener, ClientListener {

    private AlertDialog imageDialog;

    private ImageView imageView;
    private Bitmap image;

    private EditText sName, sCity, sAddress, sDescription;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_service);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(this);

        PictureSelector pictureSelector = new PictureSelector(this);
        imageDialog = pictureSelector.buildImageDialog();

        sName = (EditText) findViewById(R.id.name);
        sCity = (EditText) findViewById(R.id.scity);
        sAddress = (EditText) findViewById(R.id.saddress);
        sDescription = (EditText) findViewById(R.id.sdescription);

        mRegister = (Button) findViewById(R.id.sregister);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image){
            imageDialog.show();
        }else if (id == R.id.sregister){
            String name = String.valueOf(sName.getText());
            String city = String.valueOf(sCity.getText());
            String address = String.valueOf(sAddress.getText());
            String description = String.valueOf(sDescription.getText());

            if (name.equalsIgnoreCase("")) {
                showError(sName, R.string.name);
            } else if (city.equalsIgnoreCase("")) {
                showError(sCity, R.string.city);
            } else if (address.equalsIgnoreCase("")) {
                showError(sAddress, R.string.address);
            } else if (description.equalsIgnoreCase("")) {
                showError(sDescription, R.string.description);
            } else {

                byte imageInByte[] = null;
                if(image != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageInByte = stream.toByteArray();
                }

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sname", name ));
                params.add(new BasicNameValuePair("scity", city ));
                params.add(new BasicNameValuePair("saddress", address ));
                params.add(new BasicNameValuePair("sdescription", description ));
                params.add(new BasicNameValuePair("simage", imageInByte.toString() ));
                params.add(new BasicNameValuePair("suserid", String.valueOf(Singleton.getSingletonInstance().userID)));

                //always don`t forget set client
                Singleton.getSingletonInstance().setClientListener(this);
                Singleton.getSingletonInstance().createNewService(params);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureSelector.REQUEST_CAMERA) {
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
                    image = bm;
                    imageView.setImageBitmap(bm);

                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PictureSelector.SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri, RegisterNewService.this);
                Bitmap bm;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, bitmapOptions);

                bm = Bitmap.createScaledBitmap(bm, 250, 250, true);
                image = bm;
                imageView.setImageBitmap(bm);
            }
        }
    }
    private String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void showError(EditText editText, int id) {
        editText.setError(getResources().getString(R.string.error)
                + getResources().getString(id).toLowerCase());
        editText.requestFocus();
    }

    @Override
    public void onRequestSent() {

    }

    @Override
    public void onDataReady(JSONObject resualt) {

    }

    @Override
    public void onRequestCancelled() {

    }
}
