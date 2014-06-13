package pl.rzeszow.wsiz.carservice.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.activity.CarsList;
import pl.rzeszow.wsiz.carservice.activity.ServiceList;
import pl.rzeszow.wsiz.carservice.model.User;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * służy do wyświetlania danych osobowych
 */
public class PersonalDataFragment extends Fragment implements ClientListener {

    private String TAG = "PersonalDataFragment";

    private User us;//!< użytkownik

    private EditText username, firstName, lastName, birthDate, phoneNumber, eMail, mCity, mAddress; //!< pola, w których tekst może być edytowany
    private RadioButton rbMan, rbWomen; //! radiobuttony do wyboru płci
    private Button btnCars, btnServices; //!< przycisk auta i serwisu

    private boolean isEditMode;//!<czy dane są w trakcie edytowania
    private boolean isDataLoaded = false;//!<czy dane są załadowane

    private MenuItem saveEdit;//!< element menu
    private Context mContext;//!< służy do przytrzymania activity

    private ProgressDialog pDialog;//!< dialog z wskaźnikiem postępu wyświetlenia danych personalnych

    private String MESSAGE;//!< string łądowania danych personalnych
    private Calendar myCalendar = Calendar.getInstance();//!< kalendarz do wyboru daty
    private DatePickerDialog.OnDateSetListener date;//!< callback do wskazania czy użytkownik wybrał datę
    /**
     * Wywoływane, gdy fragment po raz pierwszy jest dołączony do jego activity.
     * @param activity activity fragmenta
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
    /**
     *  Wywoływane, gdy aktywność zaczyna.
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * Wywoływane żeby mieć instancję fragmentu w interfejsie użytkownika
     * <p>
     *     Utworzenie callbacka do wskazania daty
     * </p>
     * @param inflater stosuje się do nadmuchania widoków w fragmencie
     * @param container Jeśli niezerowy, to jest widok z rodziców, do którego fragment UI powinien
     *                  być dołączony.
     * @param savedInstanceState Jeśli niezerowy, fragment ten jest zbudowany z poprzedniego nowo zapisanego stanu.
     * @return widok dla UI fragmentu albo null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_persondata, container, false);

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

        username = (EditText) rootView.findViewById(R.id.username);
        firstName = (EditText) rootView.findViewById(R.id.first_name);
        lastName = (EditText) rootView.findViewById(R.id.last_name);
        birthDate = (EditText) rootView.findViewById(R.id.birthdate);

        birthDate.setOnClickListener(new View.OnClickListener() {
            /**
             * Kliknięcie na kalendarz
             * <p>
             *     pokazuje klikniętą datę
             * </p>
             * @param v widok, który został kliknięty.
             */
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        phoneNumber = (EditText) rootView.findViewById(R.id.phone_number);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        eMail = (EditText) rootView.findViewById(R.id.e_mail);
        mCity = (EditText) rootView.findViewById(R.id.city);
        mAddress = (EditText) rootView.findViewById(R.id.address);

        rbMan = (RadioButton) rootView.findViewById(R.id.rb_men);
        rbWomen = (RadioButton) rootView.findViewById(R.id.rb_women);

        btnCars = (Button)rootView.findViewById(R.id.ownCars);
        btnServices = (Button) rootView.findViewById(R.id.ownServices);

        btnServices.setOnClickListener(new View.OnClickListener() {
            /**
             * Kliknięcie na listę serwisów
             * <p>
             *     Startuje nowe activity z listą serwisów
             * </p>
             * @param v widok, który został kliknięty.
             */
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ServiceList.class);
                i.putExtra(Constants.USER_ID, Singleton.getSingletonInstance().userID);
                getActivity().startActivity(i);
            }
        });

        btnCars.setOnClickListener(new View.OnClickListener() {
            /**
             * Kliknięcie na listę samochodów
             * <p>
             *     Startuje nowe activity z listą samochodów
             * </p>
             * @param v widok, który został kliknięty.
             */
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CarsList.class);
                i.putExtra(Constants.USER_ID, Singleton.getSingletonInstance().userID);
                getActivity().startActivity(i);
            }
        });

        isEditMode = false;

        setHasOptionsMenu(true);
        return rootView;
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
     * Pokazywanie błądu i ustawienie fokusu na puste pole
     * @param editText pole, które jest puste
     * @param id id pola
     */
    private void showError(EditText editText, int id) {
        editText.setError(getResources().getString(R.string.error)
                + getResources().getString(id).toLowerCase());
        editText.requestFocus();
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
     * Ustawienie wskazówki na temat tego, czy fragment UI jest obecnie widoczny dla użytkownika.
     * <p>
     *     Załadowanie danych użytkownika po raz pierwszy i jeżeli Singleton jest online pobieranie
     *    dane osobiste po id w innym wypadku pokazujemy że trzeba się połączyć z internetem
     * </p>
     * @param isVisibleToUser true, jeśli fragment jest aktualnie widoczny dla użytkownika (domyślnie), false jeśli nie jest.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isDataLoaded) {
            Log.d(TAG, "Load User Data First Time");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().userID)));
            MESSAGE = getString(R.string.loading_personal_data);
            if (Singleton.isOnline(mContext)) {
                Singleton.getSingletonInstance().setClientListener(this);
                Singleton.getSingletonInstance().getPersonalData(params);
            } else {
                Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * Zainicjować zawartość menu activity
     * @param menu menu, w którym można umieścić opcje.
     * @param inflater tworzenie instancji plików XML do obiektów menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_personal, menu);
        saveEdit = menu.findItem(R.id.action_save_edit);
        setState(isEditMode);
    }
    /**
     * Wywoływane, kiedy wybrany jest element w menu.
     * @param item Element menu, który został wybrany
     * @return false, aby umożliwić normalne przetwarzanie menu, true żeby go spożywać
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_edit) {
            isEditMode = !isEditMode;
            setState(isEditMode);

            if (!isEditMode)
                updateUserData();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Wywoływane, gdy widok wcześniej stworzony przez onCreateView został odłączony od fragmentu.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    /**
     *  Wywoływane gdy żądanie zostało wyslane
     * <p>
     *     Tworzymy i pokazujemy dialog z wskaźnikiem postępu
     *     łądowania danych osobowych, Wyłączamy tryb nieokreślony dla tego okna i możliwośc
     *     anulowania okna klawiszem BACK.
     * </p>
     */
    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(MESSAGE);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
    /**
     * Wywołane kiedy dane są przeanalizowane
     * <p>
     *  Usuwamy okno z ekranu i anulujemy wszelkie wizualne wskazanie odświeżenia.
     *  Parsujemy ten rezult za pomocą JSONInterpretera.
     *  Jeżeli rezult nie jest null to znaczy że dane są załądowane,w innym wypadku
     *  aktualizujemy dane osobowe
     * </p>
     * @param resualt odpowiedż strony internetowej u postaci JSONobiektu
     */
    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
        User user = JSONInterpreter.parseUser(resualt);
        if (user != null) {
                //loading user data task ended
                isDataLoaded = true;
                us = user;
            setUserData();
        } else {
            //updating user data task ended
            Pair<Integer, String> ires = JSONInterpreter.parseMessage(resualt);
            String message = ires.second;
            if (ires.first == 1) {
                Log.d(TAG, "Personal data updated");
                us = new User(
                            String.valueOf(username.getText()),
                            null,
                            String.valueOf(firstName.getText()),
                            String.valueOf(lastName.getText()),
                            rbMan.isChecked() ? 1 : 2,
                            String.valueOf(birthDate.getText()),
                            String.valueOf(phoneNumber.getText()),
                            String.valueOf(eMail.getText()),
                            String.valueOf(mCity.getText()),
                            String.valueOf(mAddress.getText())
                        );
            } else {
                Log.d(TAG, "Update Failure!");
            }
            if (message != null) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
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
     * Ustawienie czy ustawienie jest włączone dla każdego pola
     * @param state stan textedytora
     */
    private void setState(boolean state) {
        username.setEnabled(state);
        firstName.setEnabled(state);
        lastName.setEnabled(state);
        birthDate.setEnabled(state);
        phoneNumber.setEnabled(state);
        eMail.setEnabled(state);
        mCity.setEnabled(state);
        mAddress.setEnabled(state);
        rbMan.setEnabled(state);
        rbWomen.setEnabled(state);

        isEditMode = state;
        if (isEditMode) {
            saveEdit.setIcon(android.R.drawable.ic_menu_save);
        } else {
            saveEdit.setIcon(android.R.drawable.ic_menu_edit);
        }
    }

    /**
     * Ustawienie danych osobowych użytkownika do odpowiednich pół
     */
    private void setUserData() {
        username.setText(us.getUsername());
        firstName.setText(us.getName());
        lastName.setText(us.getSurname());
        birthDate.setText(us.getBirth());
        phoneNumber.setText(us.getNr_tel());
        eMail.setText(us.getEmail());
        mCity.setText(us.getCity());
        mAddress.setText(us.getAdress());

        if (us.getSex() == 1)
            rbMan.setChecked(true);
        else if (us.getSex() == 2)
            rbWomen.setChecked(true);
    }

    /**
     * Aktualizacja danych użytkownika
     * <p>
     *    Konwertuje w string to co zostało wpisane w polach,
     * jeżeli pola są puste, będzie pokazywany błąd na tym polu,
     * jakie jest puste. W przeciwnym razie tworzy się lista z
     * kluczem i wartością i są dodawane do niej parametry z tekstedytorów.
     *  Za pomocą Singletona wykonijemy akcję
     * </p>
     */
    private void updateUserData() {
        MESSAGE = getString(R.string.update_personal_data);

        String user = String.valueOf(username.getText());
        String fname = String.valueOf(firstName.getText());
        String lname = String.valueOf(lastName.getText());
        String birth = String.valueOf(birthDate.getText());
        String phone = String.valueOf(phoneNumber.getText());
        String mail = String.valueOf(eMail.getText());
        String city = String.valueOf(mCity.getText());
        String address = String.valueOf(mAddress.getText());

        if (user.equalsIgnoreCase("")) {
            showError(username, R.string.username);
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
            int genre = rbMan.isChecked() ? 1 : 2;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().userID)));
            params.add(new BasicNameValuePair("username", user));
            params.add(new BasicNameValuePair("name", fname));
            params.add(new BasicNameValuePair("surname", lname));
            params.add(new BasicNameValuePair("sex", Integer.toString(genre)));
            params.add(new BasicNameValuePair("birth", birth));
            params.add(new BasicNameValuePair("phone", phone));
            params.add(new BasicNameValuePair("email", mail));
            params.add(new BasicNameValuePair("city", city));
            params.add(new BasicNameValuePair("adress", address));

            if (Singleton.isOnline(mContext)) {
                Singleton.getSingletonInstance().setClientListener(this);
                Singleton.getSingletonInstance().updatePersonalData(params);
            } else {
                Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            }
        }
    }
}
