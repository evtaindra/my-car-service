package pl.rzeszow.wsiz.carservice.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.adapters.ConversationListAdapter;
import pl.rzeszow.wsiz.carservice.model.BaseListItem;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class ConversationListFragment extends Fragment implements
        ClientListener,
        AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener{

    private String TAG = "ConversationListFragment";

    private Context mContext;
    private ProgressDialog pDialog;

    private ArrayList<BaseListItem> conversations;
    private ExpandableListView conversationsListView;
    private static ConversationListAdapter conversationsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean isListLoaded = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationsListAdapter = new ConversationListAdapter(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_converslist, container, false);
        Log.d(TAG, "onCreateView");

        conversationsListView = (ExpandableListView) rootView.findViewById(R.id.conversationList);
        conversationsListView.setAdapter(conversationsListAdapter);
        conversationsListView.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2, R.color.color1, R.color.color2);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isListLoaded) {
            Log.d(TAG, "Load conversation list");

            if (Singleton.isOnline(mContext)) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().userID)));
                Singleton.getSingletonInstance().setClientListener(this);
                Singleton.getSingletonInstance().getUserConversations(params);
            } else
                Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.loading_conversation_list));
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

    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);

        conversations = JSONInterpreter.parseConversationList(resualt);;

        if (conversations == null) {
            Toast.makeText(mContext, getString(R.string.alert_connection_problem), Toast.LENGTH_LONG).show();
        } else if (conversations.isEmpty()) {
            Toast.makeText(mContext, mContext.getString(R.string.alert_no_convrsations), Toast.LENGTH_LONG).show();
        } else {
            conversationsListAdapter.clearData();
            conversationsListAdapter.addConversations(conversations);
            isListLoaded=true;
        }
    }

    @Override
    public void onRequestCancelled() {
        if (pDialog != null)
            pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {
        if (Singleton.isOnline(mContext)) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().userID)));
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getUserConversations(params);
        } else {
            Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
