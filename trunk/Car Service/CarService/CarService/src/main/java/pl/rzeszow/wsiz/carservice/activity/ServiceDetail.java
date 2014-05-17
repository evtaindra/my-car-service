package pl.rzeszow.wsiz.carservice.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
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
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by rsavk_000 on 5/2/2014.
 */
public class ServiceDetail extends Activity implements ClientListener, RatingBar.OnRatingBarChangeListener {

    private final String TAG = "ServiceDetail";

    private long serviceID;

    private ImageView serviceImage;
    private TextView serviceName, serviceDescription, serviceCity, serviceAddress, servicePhone, serviceEmail;
    private RatingBar serviceRating;
    private Service mService;
    private boolean isRatingLoaded;

    private ProgressDialog pDialog;

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
        params.add(new BasicNameValuePair("sr_id", Long.toString(serviceID)));
        Singleton.getSingletonInstance().getServiceDetails(params);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (isRatingLoaded) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().userID)));
            params.add(new BasicNameValuePair("sr_id", Integer.toString(mService.getId())));
            params.add(new BasicNameValuePair("mark", Float.toString(rating)));

            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().rateService(params);
            isRatingLoaded = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_contact) {
            if (Singleton.getSingletonInstance().userID == mService.getUs_id())
                return false;
            else {
                //send message dialog
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading_service_details));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
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
                Toast.makeText(this, getString(R.string.rate_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.failed_to_rate_service), Toast.LENGTH_LONG).show();
            }
            isRatingLoaded = true;
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

        if (Singleton.getSingletonInstance().userID == mService.getUs_id()) {
            serviceRating.setEnabled(false);
        }
    }
}
