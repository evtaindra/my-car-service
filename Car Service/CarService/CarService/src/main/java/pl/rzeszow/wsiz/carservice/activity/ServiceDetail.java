package pl.rzeszow.wsiz.carservice.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.fragments.SendMessageFragment;
import pl.rzeszow.wsiz.carservice.fragments.ServiceListFragment;
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.image.PictureSelector;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by rsavk_000 on 5/2/2014.
 */
public class ServiceDetail extends ActionBarActivity implements
        ClientListener,
        RatingBar.OnRatingBarChangeListener,
        SendMessageFragment.FragmentCallBack {

    private final String TAG = "ServiceDetail";

    private long serviceID;

    private ImageView serviceImage;
    private TextView serviceName, serviceDescription, serviceCity, serviceAddress, servicePhone, serviceEmail;
    private RatingBar serviceRating;
    private Service mService;
    private boolean isRatingLoaded;

    private ProgressDialog pDialog;

    private SendMessageFragment contactDialog;

    private AlertDialog pickDialog;
    private PictureSelector pictureSelector;
    private String MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        if (getIntent() != null)
            serviceID = getIntent().getExtras().getLong(Constants.SERVICE_ID);

        serviceImage = (ImageView) findViewById(R.id.serviceImage);
        serviceName = (TextView) findViewById(R.id.serviceName);
        serviceDescription = (TextView) findViewById(R.id.serviceDescription);
        serviceCity = (TextView) findViewById(R.id.serviceCity);
        serviceAddress = (TextView) findViewById(R.id.serviceAddress);
        servicePhone = (TextView) findViewById(R.id.servicePhone);
        serviceEmail = (TextView) findViewById(R.id.serviceEmail);
        serviceRating = (RatingBar) findViewById(R.id.serviceRating);

        serviceRating.setOnRatingBarChangeListener(this);
        isRatingLoaded = false;

        Singleton.getSingletonInstance().setClientListener(this);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        MESSAGE = getString(R.string.loading_service_details);
        params.add(new BasicNameValuePair("sr_id", Long.toString(serviceID)));
        Singleton.getSingletonInstance().getServiceDetails(params);

        pictureSelector = new PictureSelector(this);
        pickDialog = pictureSelector.buildImageDialog();
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (isRatingLoaded) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().userID)));
            params.add(new BasicNameValuePair("sr_id", Integer.toString(mService.getId())));
            params.add(new BasicNameValuePair("mark", Float.toString(rating)));

            MESSAGE = getString(R.string.rating_service);
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().rateService(params);
            isRatingLoaded = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Singleton.getSingletonInstance().userID != 0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_details, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_contact) {
            if (Singleton.getSingletonInstance().userID == mService.getUs_id()) {
                Toast.makeText(this, getString(R.string.contact_not_allowed), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Bundle arg = new Bundle();
                arg.putInt("userID", Singleton.getSingletonInstance().userID);
                arg.putInt("serviceID", mService.getId());
                arg.putInt("sender", 1);
                arg.putBoolean("isDialog",true);
                contactDialog = new SendMessageFragment();
                contactDialog.setArguments(arg);
                contactDialog.show(getSupportFragmentManager(), null);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(MESSAGE);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
        if (resualt == null) {
            Toast.makeText(this, getString(R.string.alert_connection_problem), Toast.LENGTH_LONG).show();
        } else {
            Service tmp = JSONInterpreter.parseService(resualt, true);
            if (tmp != null) {
                mService = tmp;
                setServiceData();
            } else {
                Pair<Integer, String> ires = JSONInterpreter.parseMessage(resualt);
                if (ires.first == 1) {
                    Log.d(TAG, "Rate Successful!");
                    mService.setRating(Float.parseFloat(ires.second));
                    serviceRating.setRating(Float.parseFloat(ires.second));
                    ServiceListFragment.updateServiceRating(mService.getId(), Double.parseDouble(ires.second));
                    Toast.makeText(this, getString(R.string.rate_success), Toast.LENGTH_SHORT).show();
                } else if (ires.first == 2) {
                    Log.d(TAG, "Send Successful");
                    Toast.makeText(this, ires.second, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, getString(R.string.op_failed), Toast.LENGTH_LONG).show();
                }
                isRatingLoaded = true;
            }
        }
    }

    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }

    private void setServiceData() {
        serviceImage.setImageBitmap(mService.getImage());
        serviceName.setText(mService.getName());
        serviceRating.setRating((float) mService.getRating());
        isRatingLoaded = true;
        serviceDescription.setText(mService.getDescription());
        serviceCity.setText(mService.getCity());
        serviceAddress.setText(mService.getAddress());
        servicePhone.setText(mService.getPhone());
        serviceEmail.setText(mService.getEmail());

        if (Singleton.getSingletonInstance().userID == mService.getUs_id() ||
                Singleton.getSingletonInstance().userID == 0) {
            serviceRating.setEnabled(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Pair<Bitmap, String> res = pictureSelector.onActivityResult(requestCode, resultCode, data);
        if (res != null) {
            contactDialog.attachmentSelected(res);
        }
    }

    @Override
    public void pickAttachment() {
        pickDialog.show();
    }

    @Override
    public void sendMessage(List<NameValuePair> params) {
        MESSAGE = getString(R.string.sending_message);
        Singleton.getSingletonInstance().setClientListener(this);
        Singleton.getSingletonInstance().sendMessage(params);
    }
}
