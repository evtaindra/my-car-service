package pl.rzeszow.wsiz.carservice.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by opryima on 2014-05-19.
 */
public class AddNewCar extends Activity implements ClientListener {

    private ProgressDialog pDialog;

    private String TAG = "AddNewCar";

    EditText make, model, regNumb, engine, mileage, fuel, color, year;
    Button addCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_car);

        make = (EditText)findViewById(R.id.make);
        model = (EditText)findViewById(R.id.model);
        regNumb = (EditText)findViewById(R.id.regNumber);
        engine = (EditText)findViewById(R.id.engine);
        mileage = (EditText)findViewById(R.id.mileage);
        fuel = (EditText)findViewById(R.id.fuel);
        color = (EditText)findViewById(R.id.color);
        year = (EditText)findViewById(R.id.year);

        addCar = (Button)findViewById(R.id.addCar);

        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmake = String.valueOf(make.getText());
                String cmodel = String.valueOf(model.getText());
                String cmile = String.valueOf(mileage.getText());
                String creg = String.valueOf(regNumb.getText());
                String cengine = String.valueOf(engine.getText());
                String cfuel = String.valueOf(fuel.getText());
                String ccolor = String.valueOf(color.getText());
                String cyear = String.valueOf(year.getText());

                if (cmake.equalsIgnoreCase("")) {
                    showError(make, R.string.make);
                } else if (cmodel.equalsIgnoreCase("")) {
                    showError(model, R.string.model);
                } else if (cmile.equalsIgnoreCase("")) {
                    showError(mileage, R.string.mileage);
                } else if (creg.equalsIgnoreCase("")) {
                    showError(regNumb, R.string.reg_number);
                } else if (cengine.equalsIgnoreCase("")) {
                    showError(engine, R.string.engine);
                } else if (cfuel.equalsIgnoreCase("")) {
                    showError(fuel, R.string.fuel);
                } else if (ccolor.equalsIgnoreCase("")) {
                    showError(color, R.string.color);
                } else if (cyear.equalsIgnoreCase("")) {
                    showError(year, R.string.year);
                }else {

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("us_id", String.valueOf(Singleton.getSingletonInstance().userID)));
                    params.add(new BasicNameValuePair("model", cmodel));
                    params.add(new BasicNameValuePair("marka", cmake));
                    params.add(new BasicNameValuePair("nr_rej", creg));
                    params.add(new BasicNameValuePair("silnik", cengine));
                    params.add(new BasicNameValuePair("przebieg", cmile));
                    params.add(new BasicNameValuePair("color", ccolor));
                    params.add(new BasicNameValuePair("fuel", cfuel));
                    params.add(new BasicNameValuePair("year", cyear));

                    //always don`t forget set client
                    Singleton.getSingletonInstance().setClientListener(AddNewCar.this);
                    Singleton.getSingletonInstance().addNewCar(params);
                }

            }
        });
    }

    private void showError(EditText editText, int id) {
        editText.setError(getResources().getString(R.string.error)
                + getResources().getString(id).toLowerCase());
        editText.requestFocus();
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
