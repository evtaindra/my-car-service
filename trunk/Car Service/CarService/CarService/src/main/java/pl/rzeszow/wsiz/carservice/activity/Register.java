package pl.rzeszow.wsiz.carservice.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;
import pl.rzeszow.wsiz.carservice.utils.Singleton;

/**
 * Klasa Register
 * <p>
 * Klasa do rejestracji użytkownika
 * </p>
 */
public class Register extends Activity implements OnClickListener, ClientListener {

    private EditText username, password, firstName, lastName, birthDate, phoneNumber, eMail, mCity, mAddress; //!< pola, w których tekst może być edytowany
    private RadioButton rbMan, rbWomen; //! radiobuttony do wyboru płci
    private Button btnRegister; //!< przycisk rejestracji

    private ProgressDialog pDialog; //!< dialog z wskaźnikiem postępu rejestracji
    private String TAG = "Register";

    private Calendar myCalendar = Calendar.getInstance();//!< kalendarz do wyboru daty
    private DatePickerDialog.OnDateSetListener date;//!< callback do wskazania czy użytkownik wybrał datę

    /**
     * Ustawienie treści do widoku i listenera do wybranej daty.
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        date = new DatePickerDialog.OnDateSetListener() {
            /**
             * Ustawienie wybranej daty
             * @param view widok związany z listenerem
             * @param year ustawoiny rok
             * @param monthOfYear ustawiony miesiąc
             * @param dayOfMonth ustawiony dzień
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText();
            }
        };

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        birthDate = (EditText) findViewById(R.id.birthdate);

        birthDate.setOnClickListener( new OnClickListener() {
            /**
             * Kliknięcie na kalendarz
             * <p>
             *     pokazuje klikniętą datę
             * </p>
             * @param v widok, który został kliknięty.
             */
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        phoneNumber = (EditText) findViewById(R.id.phone_number);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        eMail = (EditText) findViewById(R.id.e_mail);
        mCity = (EditText) findViewById(R.id.city);
        mAddress = (EditText) findViewById(R.id.address);

        rbMan = (RadioButton) findViewById(R.id.rb_men);
        rbWomen = (RadioButton) findViewById(R.id.rb_women);

        btnRegister = (Button) findViewById(R.id.register);
        btnRegister.setOnClickListener(this);
    }

    /**
     * Rejestracja użytkownika
     * <p>
     * Konwertuje w string to co zostało wpisane w polach,
     * jeżeli pola są puste, będzie pokazywany błąd na tym polu,
     * jakie jest puste. W przeciwnym razie tworzy się lista z
     * kluczem i wartością i są dodawane do niej parametry z tekstedytorów.
     *  Za pomocą Singletona wykonijemy akcję
     * </p>
     * @param v widok, który został kliknięty.
     */
    @Override
    public void onClick(View v) {
        String user = String.valueOf(username.getText());
        String pass = String.valueOf(password.getText());
        String fname = String.valueOf(firstName.getText());
        String lname = String.valueOf(lastName.getText());
        String birth = String.valueOf(birthDate.getText());
        String phone = String.valueOf(phoneNumber.getText());
        String mail = String.valueOf(eMail.getText());
        String city = String.valueOf(mCity.getText());
        String address = String.valueOf(mAddress.getText());

        if (user.equalsIgnoreCase("")) {
            showError(username, R.string.username);
        } else if (pass.equalsIgnoreCase("")) {
            showError(password, R.string.password);
        } else if (fname.equalsIgnoreCase("")) {
            showError(firstName, R.string.first_name);
        } else if (lname.equalsIgnoreCase("")) {
            showError(lastName, R.string.last_name);
        } else if (birth.equalsIgnoreCase("")) {
            showError(birthDate, R.string.birthdate);
        } else if (phone.equalsIgnoreCase("")) {
            showError(phoneNumber, R.string.phone_number);
        } else if (!isValidEmail(mail)) {
            eMail.requestFocus();
            eMail.setError(getResources().getString(R.string.e_mail_error));
        } else if (city.equalsIgnoreCase("")) {
            showError(mCity, R.string.city);
        } else if (address.equalsIgnoreCase("")) {
            showError(mAddress, R.string.address);
        } else {

            int genre = rbMan.isChecked() ?  1 : 2 ;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", user ));
            params.add(new BasicNameValuePair("password", pass ));
            params.add(new BasicNameValuePair("name", fname ));
            params.add(new BasicNameValuePair("surname", lname ));
            params.add(new BasicNameValuePair("sex", Integer.toString(genre)));
            params.add(new BasicNameValuePair("birth", birth));
            params.add(new BasicNameValuePair("phone", phone));
            params.add(new BasicNameValuePair("email", mail ));
            params.add(new BasicNameValuePair("city", city ));
            params.add(new BasicNameValuePair("adress", address ));

            //always don`t forget set client
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().createNewUser(params);
        }
    }

    /**
     * Pokazywanie okna z dialogiem i wskaźnikiem postępu rejestacji
     * <p>
     *     Wyłączenie trybu nieokreślonego dla tego okna
     *     Możliwośc okna anulować klawiszem BACK.
     * </p>
     */
    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(Register.this);
        pDialog.setMessage("Creating User...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Wywołane kiedy dane są przeanalizowane
     * <p>
     *  Usuwamy okno z ekranu. Parsujemy ten rezult za pomocą JSONInterpretera
     *  Jeżeli w rezult integer == 1 to znaczy że użytkownik został zarejestrowany,
     *  W przeciwnym wypadku użytkownik nie został zarejestrowany. I pokazujemy o tym wiadomość
     * </p>
     * @param resualt odpowiedż strony internetowej u postaci JSONobiektu
     */
    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();

        Pair<Integer,String> ires = JSONInterpreter.parseMessage(resualt);

        String message = ires.second;

            if (ires.first == 1) {
                Log.d(TAG, "User created");
                finish();

            } else {
                Log.d(TAG, "Login Failure!");
            }

        if (message != null) {
            Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Usunięcie okna z ekranu.
     */
    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }

    /**
     * Ustawienie daty urodzenie z kalendarza
     */
    private void updateEditText() {

        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthDate.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * Sprawdzanie ważności hasła
     * @param target uporządkowany zbiór znaków
     * @return czy hasło jest słuszne
     */
    public boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Pokazywanie błądu i ustawienie fokusu na puste pole
     * @param editText pole, które jest puste
     * @param id id pola
     */
    private void showError(EditText editText, int id) {
        editText.setError(getResources().getString(R.string.error)
                + getResources().getString(id).toLowerCase());
        editText.requestFocus();
    }
}