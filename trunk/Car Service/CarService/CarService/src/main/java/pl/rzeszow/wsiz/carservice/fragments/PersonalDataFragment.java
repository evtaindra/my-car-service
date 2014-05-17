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
    private Button btnCars;

    private boolean isEditMode;
    private boolean isDataLoaded = false;

    private MenuItem saveEdit;
    private Context mContext;

    private ProgressDialog pDialog;

    private String MESSAGE;
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_persondata, container, false);

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

        username = (EditText) rootView.findViewById(R.id.username);
        firstName = (EditText) rootView.findViewById(R.id.first_name);
        lastName = (EditText) rootView.findViewById(R.id.last_name);
        birthDate = (EditText) rootView.findViewById(R.id.birthdate);

        birthDate.setOnClickListener(new View.OnClickListener() {
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

        btnCars.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isDataLoaded) {
            Log.d(TAG, "Load User Data First Time");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().getUserId())));
            MESSAGE = getString(R.string.loading_personal_data);
            if (Singleton.isOnline(mContext)) {
                Singleton.getSingletonInstance().setClientListener(this);
                Singleton.getSingletonInstance().getPersonalData(params);
            } else {
                Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_personal, menu);
        saveEdit = menu.findItem(R.id.action_save_edit);
        setState(isEditMode);
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(MESSAGE);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

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

    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }

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

            if (Singleton.isOnline(mContext)) {
                Singleton.getSingletonInstance().setClientListener(this);
                Singleton.getSingletonInstance().updatePersonalData(params);
            } else {
                Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            }
        }
    }
}
