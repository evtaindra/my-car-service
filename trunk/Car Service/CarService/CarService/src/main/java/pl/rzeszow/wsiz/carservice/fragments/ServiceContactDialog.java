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
import android.widget.Toast;

import org.json.JSONObject;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;

/**
 * Created by rsavk_000 on 5/19/2014.
 */
public class ServiceContactDialog extends DialogFragment implements View.OnClickListener,ClientListener{

    private ImageButton btnAddAttachment, btnRemoveAttachment;
    private Button btnSend;
    private TextView attachmentName;
    private EditText messageContent;

    private Context mContext;
    private Pair<Bitmap,String> attachment;

    private int senderID;
    private int recipientID;
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
            senderID = dialogArg.getInt("sender");
            recipientID = dialogArg.getInt("recipient");
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
                     Toast.makeText(mContext,"Not implemented",Toast.LENGTH_SHORT).show();
                    //Todo perform send task
                break;
        }
    }

    public void attachmentSelected(Pair<Bitmap,String> attachment ){
        this.attachment = attachment;
        attachmentName.setText(attachment.second);
    }

    @Override
    public void onRequestSent() {

    }

    @Override
    public void onDataReady(JSONObject resualt) {

    }

    @Override
    public void onRequestCancelled() {

    }

    public interface DialogCallBack{
        public void pickAttachment();
    }
}
