package pl.rzeszow.wsiz.carservice.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Pair;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.adapters.MessagesAdapter;
import pl.rzeszow.wsiz.carservice.fragments.SendMessageFragment;
import pl.rzeszow.wsiz.carservice.model.Message;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.image.PictureSelector;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Created by rsavk_000 on 5/24/2014.
 */
public class Conversation extends ActionBarActivity implements
        ClientListener,
        SwipeRefreshLayout.OnRefreshListener,
        SendMessageFragment.FragmentCallBack {

    private int userID;
    private int serviceID;
    private int sender;

    private ProgressDialog pDialog;

    private AlertDialog pickDialog;
    private PictureSelector pictureSelector;
    private SendMessageFragment contactFragment;
    private String MESSAGE;

    private ArrayList<Message> messages;
    private ListView messagesListView;
    private static MessagesAdapter messagesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conversation);
        Bundle arg = getIntent().getExtras();
        if (arg != null) {
            userID = arg.getInt("userID");
            serviceID = arg.getInt("serviceID");
            sender = arg.getInt("sender");
            arg.putBoolean("isDialog", false);
        }
        contactFragment = new SendMessageFragment();
        contactFragment.setArguments(arg);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, contactFragment).commit();

        pictureSelector = new PictureSelector(this);
        pickDialog = pictureSelector.buildImageDialog();

        messagesAdapter = new MessagesAdapter(this, sender);
        messagesListView = (ListView) findViewById(R.id.messageList);
        messagesListView.setAdapter(messagesAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2, R.color.color1, R.color.color2);

        MESSAGE = getString(R.string.loading_conversation);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("us_id", String.valueOf(userID)));
        params.add(new BasicNameValuePair("sr_id", String.valueOf(serviceID)));
        if (Singleton.isOnline(this)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getConversation(params);
        } else
            Toast.makeText(this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Pair<Bitmap, String> res = pictureSelector.onActivityResult(requestCode, resultCode, data);
        if (res != null) {
            contactFragment.attachmentSelected(res);
        }
    }

    @Override
    public void onRefresh() {
        MESSAGE = getString(R.string.loading_conversation);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("us_id", String.valueOf(userID)));
        params.add(new BasicNameValuePair("sr_id", String.valueOf(serviceID)));
        if (Singleton.isOnline(this)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getConversation(params);
        } else {
            Toast.makeText(this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(MESSAGE);
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

        if (resualt == null)
            Toast.makeText(this, getString(R.string.alert_connection_problem), Toast.LENGTH_LONG).show();
        else {
            messages = JSONInterpreter.parseConversation(resualt);
            if (messages == null) {
                Pair<Integer, String> ires = JSONInterpreter.parseMessage(resualt);
                if (ires.first == 2) {
                    Toast.makeText(this, ires.second, Toast.LENGTH_LONG).show();
                }
            } else {
                messagesAdapter.clearData();
                messagesAdapter.addServices(messages);
            }
        }
    }

    @Override
    public void onRequestCancelled() {
        if (pDialog != null)
            pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);
    }

}
