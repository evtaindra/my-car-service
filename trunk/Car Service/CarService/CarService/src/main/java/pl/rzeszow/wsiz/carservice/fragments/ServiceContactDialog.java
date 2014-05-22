package pl.rzeszow.wsiz.carservice.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
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
public class ServiceContactDialog extends DialogFragment implements View.OnClickListener{

    private ImageButton btnAddAttachment, btnRemoveAttachment;
    private Button btnSend;
    private TextView attachmentName;
    private EditText messageContent;

    private Context mContext;
    private Pair<Bitmap,String> attachment;

    private int senderID;
    private int recipientID;
    private int sender;
    private DialogCallBack dialogCallBack;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dialogCallBack = (DialogCallBack) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle dialogArg = getArguments();
        if(dialogArg!=null) {
            senderID = dialogArg.getInt("senderID");
            recipientID = dialogArg.getInt("recipientID");
            sender = dialogArg.getInt("sender");
        }
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service_contact, null);

        getDialog().setTitle(R.string.contact_to_service_title);

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
        switch (id){
            case R.id.btn_add_attachment:
                    dialogCallBack.pickAttachment();
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
                        params.add(new BasicNameValuePair("us_id", String.valueOf(senderID)));
                        params.add(new BasicNameValuePair("sr_id", String.valueOf(recipientID)));
                        params.add(new BasicNameValuePair("sender", String.valueOf(sender)));
                        params.add(new BasicNameValuePair("message", message));
                        params.add(new BasicNameValuePair("attach", attachment == null ? null :
                                BitmapEnDecode.BitmapToString(attachment.first)));

                        dialogCallBack.sendMessage(params);
                    }
                dismiss();
                break;
        }
    }

    public void attachmentSelected(Pair<Bitmap,String> attachment ){
        this.attachment = attachment;
        attachmentName.setText(attachment.second);
    }

    public interface DialogCallBack{
        public void pickAttachment();

        public void sendMessage(List<NameValuePair> params);
    }
}
