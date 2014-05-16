package pl.rzeszow.wsiz.carservice.activity;

import android.app.ProgressDialog;
import android.content.Context;
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

import org.json.JSONObject;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.adapters.CarListAdapter;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by opryima on 2014-05-16.
 */
public class CarsList extends ActionBarActivity implements ClientListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{

    private String TAG = "CarListActivity";

    private CarListAdapter carListAdapter;
    private ListView carListView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.cralist);

        carListAdapter = new CarListAdapter(this);

        if (Singleton.isOnline(this)) {
           /* Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getAllServices(null);*/
        } else
            Toast.makeText(this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();

        carListView = (ListView) findViewById(R.id.carList);
        carListView.setAdapter(carListAdapter);
        carListView.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayoutCar);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2, R.color.color1, R.color.color2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if(id == R.id.action_logout){
            getSharedPreferences(Constants.LOGIN, Context.MODE_PRIVATE).edit().clear().commit();
            finish();
            startActivity(new Intent(Guest.this, Login.class));
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        if (Singleton.isOnline(this)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getAllServices(null);
        } else {
            Toast.makeText(this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*Intent i = new Intent(mContext, ServiceDetail.class);
        i.putExtra(Constants.SERVICE_ID, id);
        mContext.startActivity(i);*/
    }

    @Override
    public void onRequestSent() {
//      swipeRefreshLayout.setRefreshing(true);
        /*if (serviceListAdapter.getCount() == 0) {
            pDialog = new ProgressDialog(mContext);
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
        }*/
    }

    @Override
    public void onDataReady(JSONObject resualt) {
       /* pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);

        services = JSONInterpreter.parseServiceList(resualt);

        if (services == null) {
            Toast.makeText(mContext, getString(R.string.alert_connection_problem), Toast.LENGTH_LONG).show();
        } else if (services.isEmpty()) {
            Toast.makeText(mContext, getString(R.string.alert_no_services), Toast.LENGTH_LONG).show();
        } else {
            serviceListAdapter.clearData();
            serviceListAdapter.addServices(services);
        }*/
    }

    @Override
    public void onRequestCancelled() {
        /*if (pDialog != null)
            pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);*/
    }
}
