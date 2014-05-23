package pl.rzeszow.wsiz.carservice.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.image.BitmapEnDecode;
import pl.rzeszow.wsiz.carservice.utils.image.PictureSelector;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by prima_000 on 2014-05-23.
 */
public class ServiceEdit extends ActionBarActivity implements ClientListener {

    Service sService;

    private AlertDialog imageDialog;
    private ProgressDialog pDialog;

    private ImageView imageView;
    private Bitmap image;

    private EditText sName, sCity, sAddress, sDescription;
    private Button sSave, sDelete;
    private PictureSelector pictureSelector;
    private String TAG = "ServiceEdit";

    private long serviceID;

    private String MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        if (getIntent() != null)
            serviceID = getIntent().getExtras().getLong(Constants.SERVICE_ID);

        MESSAGE = getString(R.string.loading_service_info);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sr_id", Long.toString(serviceID)));

        if (Singleton.isOnline(this)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getServiceDetails(params);
        } else {
            Toast.makeText(this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
        }

        imageView = (ImageView) findViewById(R.id.simage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog.show();
            }
        });

        pictureSelector = new PictureSelector(this);
        imageDialog = pictureSelector.buildImageDialog();

        sName = (EditText) findViewById(R.id.name);
        sCity = (EditText) findViewById(R.id.scity);
        sAddress = (EditText) findViewById(R.id.saddress);
        sDescription = (EditText) findViewById(R.id.sdescription);

        sSave = (Button) findViewById(R.id.ssave);
        sSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    params.add(new BasicNameValuePair("sr_id", String.valueOf(serviceID)));
                    params.add(new BasicNameValuePair("name", name));
                    params.add(new BasicNameValuePair("city", city));
                    params.add(new BasicNameValuePair("adress", address));
                    params.add(new BasicNameValuePair("opis", description));
                    params.add(new BasicNameValuePair("image", BitmapEnDecode.BitmapToString(image)));

                    //always don`t forget set client
                    Singleton.getSingletonInstance().setClientListener(ServiceEdit.this);
                    Singleton.getSingletonInstance().updateServiceInfo(params);
                }
            }
        });

        sDelete = (Button)findViewById(R.id.sdelete);
        sDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm = pictureSelector.onActivityResult(requestCode, resultCode, data).first;
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
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(MESSAGE);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }

    @Override
    public void onDataReady(JSONObject resualt) {
        Service tmp = JSONInterpreter.parseService(resualt, true);
        if (tmp != null) {
            sService = tmp;
            setServiceInfo();
        }else{
            Pair<Integer, String> ires = JSONInterpreter.parseMessage(resualt);

            String message = ires.second;

            if (ires.first == 1) {
                Log.d(TAG, "Service updated");
                finish();

            } else {
                Log.d(TAG, "Failed to update a service!");
            }

            if (message != null) {
                Toast.makeText(ServiceEdit.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setServiceInfo(){
        sName.setText(sService.getName());
        sCity.setText(sService.getCity());
        sAddress.setText(sService.getAddress());
        sDescription.setText(sService.getDescription());
        imageView.setImageBitmap(sService.getImage());
    }
}
