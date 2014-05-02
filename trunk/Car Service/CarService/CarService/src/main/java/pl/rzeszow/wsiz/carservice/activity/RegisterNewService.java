package pl.rzeszow.wsiz.carservice.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.image.BitmapEnDecode;
import pl.rzeszow.wsiz.carservice.utils.image.PictureSelector;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class RegisterNewService extends Activity implements View.OnClickListener, ClientListener {

    private AlertDialog imageDialog;
    private ProgressDialog pDialog;

    private ImageView imageView;
    private Bitmap image;

    private EditText sName, sCity, sAddress, sDescription;
    private Button mRegister;
    private PictureSelector pictureSelector;
    private String TAG = "RegisterNewService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_service);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(this);

        pictureSelector = new PictureSelector(this);
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
        if (id == R.id.image) {
            imageDialog.show();
        } else if (id == R.id.sregister) {
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

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sname", name));
                params.add(new BasicNameValuePair("scity", city));
                params.add(new BasicNameValuePair("saddress", address));
                params.add(new BasicNameValuePair("sdescription", description));
                params.add(new BasicNameValuePair("simage", BitmapEnDecode.BitmapToString(image)));
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
        Bitmap bm = pictureSelector.onActivityResult(requestCode, resultCode, data);
        if (bm != null) {
            image = bm;
            imageView.setImageBitmap(bm);
        }
    }

    private void showError(EditText editText, int id) {
        editText.setError(getResources().getString(R.string.error)
                + getResources().getString(id).toLowerCase());
        editText.requestFocus();
    }

    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(RegisterNewService.this);
        pDialog.setMessage("Creating Service...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();

        Pair<Integer, String> ires = JSONInterpreter.parseMessage(resualt);

        String message = ires.second;

        if (ires.first == 1) {
            Log.d(TAG, "Service created");
            finish();

        } else {
            Log.d(TAG, "Failed to create a service!");
        }

        if (message != null) {
            Toast.makeText(RegisterNewService.this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }
}
