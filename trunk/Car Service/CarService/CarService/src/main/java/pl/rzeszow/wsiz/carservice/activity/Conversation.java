package pl.rzeszow.wsiz.carservice.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.apache.http.NameValuePair;

import java.util.List;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.fragments.SendMessageFragment;

/**
 * Created by rsavk_000 on 5/24/2014.
 */
public class Conversation extends ActionBarActivity implements SendMessageFragment.FragmentCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conversation);
        Bundle arg = new Bundle();
        arg.putInt("senderID", 0);
        arg.putInt("recipientID", 0);
        arg.putInt("sender", 0);
        arg.putBoolean("isDialog",false);
        SendMessageFragment fragment = new SendMessageFragment();
        fragment.setArguments(arg);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void pickAttachment() {

    }

    @Override
    public void sendMessage(List<NameValuePair> params) {

    }
}
