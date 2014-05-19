package pl.rzeszow.wsiz.carservice.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import org.json.JSONObject;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by opryima on 2014-05-19.
 */
public class AddNewCar extends Activity implements ClientListener {

    private ProgressDialog pDialog;

    private String TAG = "AddNewCar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_car);
    }

    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(AddNewCar.this);
        pDialog.setMessage("Adding car...");
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
            Log.d(TAG, "Car added");
            finish();

        } else {
            Log.d(TAG, "Failed to add a car!");
        }

        if (message != null) {
            Toast.makeText(AddNewCar.this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }
}
