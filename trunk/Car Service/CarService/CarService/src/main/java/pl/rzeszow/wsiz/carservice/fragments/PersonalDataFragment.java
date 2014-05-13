package pl.rzeszow.wsiz.carservice.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.jar.Manifest;

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

    private ArrayList<User> user;

    private Context mContext;

    private EditText username, password, firstName, lastName, birthDate, phoneNumber, eMail, mCity, mAddress;
    private RadioButton rbMan, rbWomen;

    private MenuItem save, edit;

    private ProgressDialog pDialog;

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;

    private SwipeRefreshLayout swipeRefreshLayout;

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
        //password = (EditText) rootView.findViewById(R.id.password);
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
        Log.i(TAG, "onSaveInstanceState");
        //outState.putBoolean("editModeBool", editModeBool);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (Singleton.getSingletonInstance().userID != 0)
            inflater.inflate(R.menu.menu_personal, menu);

        Log.d(TAG, "onCreateOptionsMenu" + Constants.editModeBool);

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
        pDialog.setMessage("Updating personal data...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();

        Pair<Integer,String> ires = JSONInterpreter.parseMessage(resualt);

        String message = ires.second;

        if (ires.first == 1) {
            Log.d(TAG, "Personal data updated");
            //finish();

        } else {
            Log.d(TAG, "Login Failure!");
        }

        if (message != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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


    private void editMode(MenuItem item)
    {
        Constants.editModeBool = false;
        username.setEnabled(true);
        //password.setEnabled(true);
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
        //password.setEnabled(false);
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

        String user = String.valueOf(username.getText());
        //String pass = String.valueOf(password.getText());
        String fname = String.valueOf(firstName.getText());
        String lname = String.valueOf(lastName.getText());
        String birth = String.valueOf(birthDate.getText());
        String phone = String.valueOf(phoneNumber.getText());
        String mail = String.valueOf(eMail.getText());
        String city = String.valueOf(mCity.getText());
        String address = String.valueOf(mAddress.getText());

        if (user.equalsIgnoreCase("")) {
            showError(username, R.string.username);
        } /*else if (pass.equalsIgnoreCase("")) {
            showError(password, R.string.password);
        }*/ else if (fname.equalsIgnoreCase("")) {
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
                //params.add(new BasicNameValuePair("password", pass));
                params.add(new BasicNameValuePair("name", fname));
                params.add(new BasicNameValuePair("surname", lname));
                params.add(new BasicNameValuePair("sex", Integer.toString(genre)));
                params.add(new BasicNameValuePair("birth", birth));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("email", mail));
                params.add(new BasicNameValuePair("city", city));
                params.add(new BasicNameValuePair("adress", address));


            //always don`t forget set client
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().updatePersonalData(params);

        }
    }

}
