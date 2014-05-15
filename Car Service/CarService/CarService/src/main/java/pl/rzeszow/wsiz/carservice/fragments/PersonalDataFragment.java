package pl.rzeszow.wsiz.carservice.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import pl.rzeszow.wsiz.carservice.model.User;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class PersonalDataFragment extends Fragment implements ClientListener {

    private String TAG = "PersonalDataFragment";

    private User us;

    private EditText username, firstName, lastName, birthDate, phoneNumber, eMail, mCity, mAddress;
    private RadioButton rbMan, rbWomen;

    private MenuItem save, edit;

    private ProgressDialog pDialog;

    private  String MESSAGE = "Loading personal data...";

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_persondata, container, false);
        Log.d(TAG, "onCreateView");

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText();
            }
        };

        username = (EditText)rootView.findViewById(R.id.username);
        firstName = (EditText) rootView.findViewById(R.id.first_name);
        lastName = (EditText) rootView.findViewById(R.id.last_name);
        birthDate = (EditText) rootView.findViewById(R.id.birthdate);

        birthDate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        phoneNumber = (EditText) rootView.findViewById(R.id.phone_number);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        eMail = (EditText) rootView.findViewById(R.id.e_mail);
        mCity = (EditText) rootView.findViewById(R.id.city);
        mAddress = (EditText) rootView.findViewById(R.id.address);

        rbMan = (RadioButton) rootView.findViewById(R.id.rb_men);
        rbWomen = (RadioButton) rootView.findViewById(R.id.rb_women);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && Constants.editModeBool) {
            Log.d(TAG, "setUserVisibleHint");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().getUserId())));
            MESSAGE = "Loading personal data...";
            if (Singleton.isOnline(getActivity())) {
                Singleton.getSingletonInstance().setClientListener(this);
                Singleton.getSingletonInstance().getPersonalData(params);
            } else {
                Toast.makeText(getActivity(), R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (Singleton.getSingletonInstance().userID != 0)
            inflater.inflate(R.menu.menu_personal, menu);

        Log.d(TAG, "onCreateOptionsMenu");

        save = menu.findItem(R.id.action_save_data);
        edit = menu.findItem(R.id.action_edit_data);

        if (Constants.editModeBool) {
            edit.setVisible(true);
            save.setVisible(false);
        }
        if(!Constants.editModeBool){
            edit.setVisible(false);
            save.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_data) {
            editMode(item);
        }
        if (id == R.id.action_save_data) {
            saveMode(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(MESSAGE);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();

        User user = JSONInterpreter.parseUserSimple(resualt);
        if (user == null) {
            Pair<Integer,String> ires = JSONInterpreter.parseMessage(resualt);

            String message = ires.second;

            if (ires.first == 1) {
                Log.d(TAG, "Personal data updated");

            } else {
                Log.d(TAG, "Login Failure!");
            }
            if (message != null) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        } else {
            MESSAGE = "Loading personal data...";
            us = user;
            setText();
        }

    }

    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }

    private void updateEditText() {

        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void showError(EditText editText, int id) {
        editText.setError(getResources().getString(R.string.error)
                + getResources().getString(id).toLowerCase());
        editText.requestFocus();
    }

    public boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void setText()
    {
        username.setText(us.getUsername());
        firstName.setText(us.getName());
        lastName.setText(us.getSurname());
        birthDate.setText(us.getBirth());
        phoneNumber.setText(us.getNr_tel());
        eMail.setText(us.getEmail());
        mCity.setText(us.getCity());
        mAddress.setText(us.getAdress());

        if(us.getSex() == 1)
            rbMan.setChecked(true);
        else if(us.getSex() == 2)
            rbWomen.setChecked(true);
    }

    private void editMode(MenuItem item)
    {
        Constants.editModeBool = false;
        username.setEnabled(true);
        firstName.setEnabled(true);
        lastName.setEnabled(true);
        birthDate.setEnabled(true);
        phoneNumber.setEnabled(true);
        eMail.setEnabled(true);
        mCity.setEnabled(true);
        mAddress.setEnabled(true);
        rbMan.setEnabled(true);
        rbWomen.setEnabled(true);
        item.setVisible(false);
        save.setVisible(true);
    }

    private void saveMode(MenuItem item)
    {
        Constants.editModeBool = true;
        username.setEnabled(false);
        firstName.setEnabled(false);
        lastName.setEnabled(false);
        birthDate.setEnabled(false);
        phoneNumber.setEnabled(false);
        eMail.setEnabled(false);
        mCity.setEnabled(false);
        mAddress.setEnabled(false);
        rbMan.setEnabled(false);
        rbWomen.setEnabled(false);
        item.setVisible(false);
        edit.setVisible(true);

        MESSAGE = "Updating personal data...";

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
                params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().getUserId())));
                params.add(new BasicNameValuePair("username", user));
                params.add(new BasicNameValuePair("name", fname));
                params.add(new BasicNameValuePair("surname", lname));
                params.add(new BasicNameValuePair("sex", Integer.toString(genre)));
                params.add(new BasicNameValuePair("birth", birth));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("email", mail));
                params.add(new BasicNameValuePair("city", city));
                params.add(new BasicNameValuePair("adress", address));

            if (Singleton.isOnline(getActivity())) {
                Singleton.getSingletonInstance().setClientListener(this);
                Singleton.getSingletonInstance().updatePersonalData(params);
            } else {
                Toast.makeText(getActivity(), R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            }
        }
    }

}
