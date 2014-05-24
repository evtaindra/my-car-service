package pl.rzeszow.wsiz.carservice.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.activity.RegisterNewService;
import pl.rzeszow.wsiz.carservice.activity.ServiceDetail;
import pl.rzeszow.wsiz.carservice.adapters.ServiceListAdapter;
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class ServiceListFragment extends Fragment implements ClientListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private String TAG = "ServiceListFragment";
    private Context mContext;
    private ProgressDialog pDialog;

    private ArrayList<Service> services;
    private ListView servicesListView;
    private static ServiceListAdapter serviceListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Log.d(TAG, "onCreate");

        serviceListAdapter = new ServiceListAdapter(mContext);

        if (Singleton.isOnline(mContext)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getAllServices(null);
        } else
            Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_servicelist, container, false);
        Log.d(TAG, "onCreateView");

        servicesListView = (ListView) rootView.findViewById(R.id.servicesList);
        servicesListView.setAdapter(serviceListAdapter);
        servicesListView.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2, R.color.color1, R.color.color2);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (Singleton.getSingletonInstance().userID != 0)
            inflater.inflate(R.menu.menu_servicelist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_new_service) {
            mContext.startActivity(new Intent(mContext, RegisterNewService.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        if (Singleton.isOnline(mContext)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getAllServices(null);
        } else {
            Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(mContext, ServiceDetail.class);
        i.putExtra(Constants.SERVICE_ID, id);
        mContext.startActivity(i);
    }

    @Override
    public void onRequestSent() {
//      swipeRefreshLayout.setRefreshing(true);
        if (serviceListAdapter.getCount() == 0) {
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
        }
    }

    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);

        if (resualt == null)
            Toast.makeText(mContext, getString(R.string.alert_connection_problem), Toast.LENGTH_LONG).show();
        else {
            services = JSONInterpreter.parseServiceList(resualt);
            if (services.isEmpty()) {
                Toast.makeText(mContext, getString(R.string.alert_no_services), Toast.LENGTH_LONG).show();
            } else {
                serviceListAdapter.clearData();
                serviceListAdapter.addServices(services);
            }
        }
    }

    @Override
    public void onRequestCancelled() {
        if (pDialog != null)
            pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);
    }

    public static void updateServiceRating(int id, double rating) {
        serviceListAdapter.updateItemRating(id, rating);
    }
}
