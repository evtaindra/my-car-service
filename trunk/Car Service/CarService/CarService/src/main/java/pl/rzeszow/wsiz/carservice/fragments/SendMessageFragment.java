package pl.rzeszow.wsiz.carservice.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.utils.image.BitmapEnDecode;

/**
 * Created by rsavk_000 on 5/19/2014.
 */
public class SendMessageFragment extends DialogFragment implements View.OnClickListener {

    private ImageButton btnAddAttachment, btnRemoveAttachment;
    private Button btnSend;
    private TextView attachmentName;
    private EditText messageContent;

    private Context mContext;
    private Pair<Bitmap, String> attachment;

    private int userID;
    private int serviceID;
    private int sender;
    private boolean isDialog;
    private FragmentCallBack fragmentCallBack;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle dialogArg = getArguments();
        if (dialogArg != null) {
            userID = dialogArg.getInt("userID");
            serviceID = dialogArg.getInt("serviceID");
            sender = dialogArg.getInt("sender");
            isDialog = dialogArg.getBoolean("isDialog");
        }
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = null;
        if (isDialog) {
            getDialog().setTitle(R.string.contact_to_service_title);
            rootView = inflater.inflate(R.layout.fragment_send_message, container);
        }else{
            rootView = inflater.inflate(R.layout.fragment_send_message, container,false);
        }

        btnAddAttachment = (ImageButton) rootView.findViewById(R.id.btn_add_attachment);
        btnAddAttachment.setOnClickListener(this);
        btnRemoveAttachment = (ImageButton) rootView.findViewById(R.id.btn_remove_attachment);
        btnRemoveAttachment.setOnClickListener(this);
        btnSend = (Button) rootView.findViewById(R.id.btn_send_message);
        btnSend.setOnClickListener(this);

        attachmentName = (TextView) rootView.findViewById(R.id.attachment_name);
        messageContent = (EditText) rootView.findViewById(R.id.message_content);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_attachment:
                fragmentCallBack.pickAttachment();
                break;
            case R.id.btn_remove_attachment:
                attachment = null;
                attachmentName.setText("");
                break;
            case R.id.btn_send_message:
                String message = String.valueOf(messageContent.getText());

                if (message.equalsIgnoreCase("")) {
                    messageContent.setError(getResources().getString(R.string.error)
                            + mContext.getString(R.string.message));
                    messageContent.requestFocus();
                } else {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("us_id", String.valueOf(userID)));
                    params.add(new BasicNameValuePair("sr_id", String.valueOf(serviceID)));
                    params.add(new BasicNameValuePair("sender", String.valueOf(sender)));
                    params.add(new BasicNameValuePair("message", message));
                    params.add(new BasicNameValuePair("attachment", attachment == null ? null :
                            BitmapEnDecode.BitmapToString(attachment.first)));

                    fragmentCallBack.sendMessage(params);

                    if (isDialog) {
                        dismiss();
                    }
                }
                break;
        }
    }

    public void attachmentSelected(Pair<Bitmap, String> attachment) {
        this.attachment = attachment;
        attachmentName.setText(attachment.second);
    }

    public interface FragmentCallBack {
        public void pickAttachment();

        public void sendMessage(List<NameValuePair> params);
    }
}
