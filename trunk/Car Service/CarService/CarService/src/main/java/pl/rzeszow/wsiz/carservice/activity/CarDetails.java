package pl.rzeszow.wsiz.carservice.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.model.Car;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by opryima on 2014-05-19.
 */
public class CarDetails extends ActionBarActivity implements ClientListener {

    Car ccar;

    private ProgressDialog pDialog;

    private String TAG = "EditCar";

    EditText make, model, regNumb, engine, mileage, fuel, color, year;
    Button save;

    private long carID;

    private String MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        if (getIntent() != null)
            carID = getIntent().getExtras().getLong(Constants.CAR_ID);

        MESSAGE = getString(R.string.loading_car_info);

        make = (EditText)findViewById(R.id.make);
        model = (EditText)findViewById(R.id.model);
        regNumb = (EditText)findViewById(R.id.regNumber);
        engine = (EditText)findViewById(R.id.engine);
        mileage = (EditText)findViewById(R.id.mileage);
        fuel = (EditText)findViewById(R.id.fuel);
        color = (EditText)findViewById(R.id.color);
        year = (EditText)findViewById(R.id.year);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nr_id", Long.toString(carID)));

        if (Singleton.isOnline(this)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getCarInfo(params);
        } else {
            Toast.makeText(this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
        }

        save = (Button)findViewById(R.id.save_edit);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MESSAGE = getString(R.string.update_car_info);
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
                    params.add(new BasicNameValuePair("nr_id", String.valueOf(carID)));
                    params.add(new BasicNameValuePair("model", cmodel));
                    params.add(new BasicNameValuePair("marka", cmake));
                    params.add(new BasicNameValuePair("nr_rej", creg));
                    params.add(new BasicNameValuePair("silnik", cengine));
                    params.add(new BasicNameValuePair("przebieg", cmile));
                    params.add(new BasicNameValuePair("color", ccolor));
                    params.add(new BasicNameValuePair("fuel", cfuel));
                    params.add(new BasicNameValuePair("year", cyear));

                    //always don`t forget set client
                    if (Singleton.isOnline(CarDetails.this)) {
                    Singleton.getSingletonInstance().setClientListener(CarDetails.this);
                    Singleton.getSingletonInstance().updateCar(params);
                    } else {
                        Toast.makeText(CarDetails.this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
                    }
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
        pDialog = new ProgressDialog(CarDetails.this);
        pDialog.setMessage(MESSAGE);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();

        Car car = JSONInterpreter.parseCar(resualt, true);

        if(car != null){
            ccar = car;

            setCarInfo();
        }else{
            Pair<Integer, String> ires = JSONInterpreter.parseMessage(resualt);

            String message = ires.second;

            if (ires.first == 1) {
                Log.d(TAG, "Car updated");
                finish();

            } else {
                Log.d(TAG, "Failed to update a car!");
            }

            if (message != null) {
                Toast.makeText(CarDetails.this, message, Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }

    private void setCarInfo(){
        make.setText(ccar.getMarka());
        model.setText(ccar.getModel());
        regNumb.setText(ccar.getNr_rej());
        engine.setText(Double.toString(ccar.getSilnik()));
        mileage.setText(Integer.toString(ccar.getPrzebieg()));
        fuel.setText(ccar.getPaliwo());
        color.setText(ccar.getKolor());
        year.setText(Integer.toString(ccar.getRok()));
    }

}
