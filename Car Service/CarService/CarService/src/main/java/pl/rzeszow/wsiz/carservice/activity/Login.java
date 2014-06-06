package pl.rzeszow.wsiz.carservice.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Klasa Login
 * <p>
 * Klasa do obsługi logowania do aplikacji
 * </p>
 */
public class Login extends ActionBarActivity implements OnClickListener, ClientListener {

    private EditText username, password; //!< pola, w których tekst może być edytowany
    private Button mSubmit, mRegister, mGuest; //!< przyciski logowania, rejestracji i wejść jako gość
    private CheckBox lRemember; //!<zapamiętania imięni i hasła

    private String TAG = "Login";  //!< zmienna przyjmująca wartość string

    private SharedPreferences mLogin;//!< służy do dostępu i modyfikowania danych
    private ProgressDialog pDialog;//!< dialog z wskaźnikiem postępu logowania
    /**
     * Wywoływane, gdy aktywność zaczyna.
     * <p>
     *      Ustawienie treści do widoku,tekstedytorów i checkboxa do widoku i listenerów
     *      dla przycisków. Pobieranie i przytrzymanie zawartości loginu.
     * </p>
     * @param savedInstanceState  Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     */
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

    /**
     * Logowanie lub rejestracja do systemu
     * <p>
     *     W przypadku naciśnięcia przycisku Login, jeśli Singleton jest online
     *     pobieramy znaczenia które zostały napisane w polach i tworzymy z tymi parametrami listę i  za pomocą Singletona
     *     wykonujemy akcję, w innym razie wystąpi bład połączenia z internetem.
     *     W przypadku rejestracji i wejścia jako gość tworzymy intencję i przechodzimy do nowego activity.
     *
     *
     * </p>
     * @param v  widok, który został kliknięty
     */
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.login:
                if (Singleton.isOnline(this)) {
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

    /**
     *  Pokazywanie okna z dialogiem i wskaźnikiem postępu logowania
     * <p>
     *     Wyłączenie trybu nieokreślonego dla tego okna
     *     Możliwośc okna anulować klawiszem BACK.
     */
    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Attempting login...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Wywołane kiedy dane są przeanalizowane
     * <p>
     *  Usuwamy okno z ekranu i parsujemy rezult za pomocą JSONInterpretera.
     *  W przypadku wystąpienia 1 logowanie sie powiodlo i jeżeli checkbox został kliknięty
     *  tworzymy interfejs do modyfikowania wartości i ustawiamy wartości logina i hasła
     *  w tym edytorze. Tworzymy nową intencję, dodajemy id użytkownika i rozpoczynamy nowe activity.
     * </p>
     * @param resualt odpowiedż strony internetowej u postaci JSONobiektu
     */
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

    /**
     * Usunięcie okna z ekranu.
     */
    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }
}