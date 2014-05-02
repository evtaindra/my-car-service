package pl.rzeszow.wsiz.carservice.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;
import pl.rzeszow.wsiz.carservice.utils.Singleton;

public class Login extends ActionBarActivity implements OnClickListener, ClientListener {

    private EditText username, password;
    private Button mSubmit, mRegister, mGuest;
    private CheckBox lRemember;

    private String TAG = "Login";

    private SharedPreferences mLogin;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        mSubmit = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);
        mGuest = (Button) findViewById(R.id.guest);

        lRemember = (CheckBox) findViewById(R.id.remember);

        mSubmit.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mGuest.setOnClickListener(this);

        mLogin = getSharedPreferences(Constants.LOGIN, Context.MODE_PRIVATE);
    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected())
            return true;

        return false;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.login:
                if (isOnline()) {
                    String user = String.valueOf(username.getText());
                    String pass = String.valueOf(password.getText());

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username", user));
                    params.add(new BasicNameValuePair("password", pass));

                    Singleton.getSingletonInstance().setClientListener(this);
                    Singleton.getSingletonInstance().attemptLogin(params);
                } else
                    Toast.makeText(Login.this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
                break;
            case R.id.register:
                i = new Intent(this, Register.class);
                startActivity(i);
                break;
            case R.id.guest:
                i = new Intent(this, Guest.class);
                finish();
                startActivity(i);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Attempting login...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void onDataReady(JSONObject resualt) {

        pDialog.dismiss();
        Pair<Integer,String> ires = JSONInterpreter.parseMessage(resualt);

        if (ires.first == 1) {
            Log.d(TAG, "Login Successful!");
            if (lRemember.isChecked()) {
                SharedPreferences.Editor editor = mLogin.edit();
                editor.putString(Constants.LOGIN_USER, String.valueOf(username.getText()));
                editor.putString(Constants.LOGIN_PASSWORD, String.valueOf(password.getText()));
                editor.putInt(Constants.LOGIN_ID, Integer.parseInt(ires.second));
                editor.commit();
            }
            Intent i = new Intent(this , MainActivity.class);
            i.putExtra(Constants.USER_ID, Integer.parseInt(ires.second));
            finish();
            startActivity(i);
        }else{
            Toast.makeText(Login.this, ires.second , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }
}