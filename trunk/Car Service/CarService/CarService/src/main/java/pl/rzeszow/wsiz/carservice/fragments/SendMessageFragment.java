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
 *  Służy do zarządzania wiadomościami
 */
public class SendMessageFragment extends DialogFragment implements View.OnClickListener {

    private ImageButton btnAddAttachment, btnRemoveAttachment;//!<Wyświetla przycisk z obrazem
    private Button btnSend;//!< przycisk wysyłania wiadomości
    private TextView attachmentName;//!<Wyświetla tekst użytkownikowi i pozwala go edytować
    private EditText messageContent; //!< pola, w których tekst może być edytowany

    private Context mContext; //!< służy do przytrzymania activity
    private Pair<Bitmap, String> attachment;//!< pojemnik dla przytrzymania załącznika

    private int userID;//!< id użytkownika
    private int serviceID;//!< id serwisu
    private int sender;//!< od kogo jest wysłana wiadomość
    private boolean isDialog; //!< czy dialog jest widoczny
    private FragmentCallBack fragmentCallBack;//!< dla komunikowania pomiędzy fragmentami

    /**
     * Wywoływane, gdy fragment po raz pierwszy jest dołączony do jego activity.
     * @param activity activity fragmenta
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack) activity;
    }
    /**
     *  Wywoływane, gdy aktywność zaczyna.
     * <p>
     *   Zapisujemy dane o id użytkownika, serwisu i o dialogu
     * </p>
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     *
     */
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
    /**
     * Wywoływane żeby mieć instancję fragmentu w interfejsie użytkownika
     * <p>
     *     ustawienie treści do dialogu
     * </p>
     * @param inflater stosuje się do nadmuchania widoków w fragmencie
     * @param container Jeśli niezerowy, to jest widok z rodziców, do którego fragment UI powinien
     *                  być dołączony.
     * @param savedInstanceState Jeśli niezerowy, fragment ten jest zbudowany z poprzedniego nowo zapisanego stanu.
     * @return widok dla UI fragmentu albo null.
     */
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
    /**
     * Wywoływane, gdy widok został kliknięty.
     * <p>
     *  Sprawdzamy jaki przycisk został kliknięty. Jeżeli dodanie załącznika
     *  to dodajemy załącznik, usuwanie załącznika usuwamy,
     *  w przypadku wysyłania wiadomości, bierzemy to co jest zapisane w wiadomości,
     *  jeżeli wiadomośc pusta pokazujemy error, w innym przypadku tworzymy listę z
     *  kluczem i wartością i dodajemy do niej parametry z tekstedytorów i
     *  wysyłamy wiadomość
     * </p>
     * @param v widok, który został kliknięty.
     */
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

    /**
     * Ustawienie załącznika do wiadomości
     * @param attachment  pojemnik dla przytrzymania załącznika
     */
    public void attachmentSelected(Pair<Bitmap, String> attachment) {
        this.attachment = attachment;
        attachmentName.setText(attachment.second);
    }

    /**
     * Intefejs dla komunikowania pomiędzy fragmentami
     */
    public interface FragmentCallBack {
        /**
         * wybór załącznika
         */
        public void pickAttachment();

        /**
         * Wysyłanie wiadomości
         * @param params lista z parametrami
         */
        public void sendMessage(List<NameValuePair> params);
    }
}
