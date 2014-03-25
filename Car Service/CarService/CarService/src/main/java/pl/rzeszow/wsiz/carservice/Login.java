package pl.rzeszow.wsiz.carservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends Activity implements OnClickListener{

    private EditText user, pass;
    private Button mSubmit, mRegister, mGuest;
    private CheckBox lRemember;

    public static final String login = "myLogin";
    public static final String login_username = "lUsername";
    public static final String login_password = "lPassword";

    SharedPreferences mLogin;

    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();

    private static final String LOGIN_URL = "http://carservice.esy.es/carserv/login.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        user = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);

        mSubmit = (Button)findViewById(R.id.login);
        mRegister = (Button)findViewById(R.id.register);
        mGuest = (Button) findViewById(R.id.guest);

        lRemember = (CheckBox)findViewById(R.id.remember);

        mSubmit.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mGuest.setOnClickListener(this);

        mLogin = getSharedPreferences(login, Context.MODE_PRIVATE);
    }

    private Boolean isOnline()  {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected())
            return true;

        return false;
    }

    @Override
    public void onClick(View v) {

        Intent i;
        switch (v.getId()) {
            case R.id.login:
                if(isOnline())
                    new AttemptLogin().execute();
                else
                    Toast.makeText(Login.this, R.string.check, Toast.LENGTH_LONG).show();
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

    class AttemptLogin extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                Log.d("Login attempt", json.toString());

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());

                    if(lRemember.isChecked())
                    {
                        SharedPreferences.Editor editor = mLogin.edit();
                        editor.putString(login_username, username);
                        editor.putString(login_password, password);
                        editor.commit();
                    }
                    Intent i = new Intent(Login.this, MainActivity.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}