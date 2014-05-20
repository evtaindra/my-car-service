package pl.rzeszow.wsiz.carservice.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.adapters.ServiceListAdapter;
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by opryima on 2014-05-20.
 */
public class ServiceList extends ActionBarActivity implements ClientListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private String TAG = "ServiceListF";

    private ProgressDialog pDialog;

    private ArrayList<Service> services;
    private ListView servicesListView;
    public static ServiceListAdapter serviceListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.fragment_servicelist);

        serviceListAdapter = new ServiceListAdapter(this);

        if (getIntent() != null)
            userID = getIntent().getExtras().getInt(Constants.USER_ID);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("us_id", Integer.toString(userID)));

        if (Singleton.isOnline(this)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getUserServices(params);
        } else
            Toast.makeText(this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();

        servicesListView = (ListView) findViewById(R.id.servicesList);
        servicesListView.setAdapter(serviceListAdapter);
        servicesListView.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2, R.color.color1, R.color.color2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_servicelist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_new_service) {
            this.startActivity(new Intent(this, RegisterNewService.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("us_id", Integer.toString(userID)));
        if (Singleton.isOnline(this)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getUserServices(params);
        } else {
            Toast.makeText(this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*Intent i = new Intent(this, CarDetails.class);
        i.putExtra(Constants.CAR_ID, id);
        this.startActivity(i);*/
    }

    @Override
    public void onRequestSent() {
//      swipeRefreshLayout.setRefreshing(true);
        if (serviceListAdapter.getCount() == 0) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.loading_services));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Singleton.getSingletonInstance().cancelCurrentTask();
                }
            });
            pDialog.show();
        }
    }

    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);

        services = JSONInterpreter.parseServiceList(resualt);

        if (services == null) {
            Toast.makeText(this, getString(R.string.alert_connection_problem), Toast.LENGTH_LONG).show();
        } else if (services.isEmpty()) {
            Toast.makeText(this, getString(R.string.alert_no_services), Toast.LENGTH_LONG).show();
        } else {
            serviceListAdapter.clearData();
            serviceListAdapter.addServices(services);
        }
    }

    @Override
    public void onRequestCancelled() {
        if (pDialog != null)
            pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);
    }
}
