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
 * Klasa Conversation
 * <p>
 * Służy do zarządzania wiadomościami
 * </p>
 */
public class Conversation extends ActionBarActivity implements
        ClientListener,
        SwipeRefreshLayout.OnRefreshListener,
        SendMessageFragment.FragmentCallBack {

    private int userID; //!< id użytkownika
    private int serviceID; //!< id serwisu
    private int sender; //!< od kogo jest przesłana wiadomość od użytkownika lub serwisu.

    private ProgressDialog pDialog; //!< dialog z wskaźnikiem postępu wyświetlenia szczeglnej informacji o serwisie

    private AlertDialog pickDialog;  //!< dialog dla dodania obrazku serwisu
    private PictureSelector pictureSelector; //!< służy do wybrania obrazku
    private SendMessageFragment contactFragment; //<!fragment dialogu pomiędzy użytkownikiem a serwisem
    private String MESSAGE; //!< zmienna przyjmująca wartość string ("Loading conversation")

    private ArrayList<Message> messages; //!<lista zawierająca wiadomości.
    private ListView messagesListView; //!< widok, który pokazuje wiadomości na liście
    private static MessagesAdapter messagesAdapter; //!< obiekt klasy MessagesAdapter do wyświetlania wiadomości na liście
    private SwipeRefreshLayout swipeRefreshLayout; //!< służy do odświeżania zawartości widoku poprzez pionowe machnięcie

    /**
     * Wywoływane, gdy aktywność zaczyna.
     * <p>
     *       Ustawienie treści do widoku. Jeżeli intencja nie jest pusta
     *       mapujemy parametry z wartości string do int. Tworzymy nowy kontaktny dialog i
     *       ustalamy tam nasze parametry. Zamieniamy to co jest w widoku container z naszym fragmentem
     *       i zatwierdzamy transakcje. Pokazyjemy dialog dla wybrania obrazku, Tworzymy adapter
     *       i widok, który będzie pokazywał wiadomości na liście, a także odświeżanie
     *       zawartości widoku i listener do niego.
     *       Tworzy się lista z kluczem i wartością i jest
     *  dodawany do niej parametr id serwisu i użytkownika. Jesli Singleton jest online, wykonijemy akcję,
     *  w innym przypadku pokazujemy wiadomość sprawdź połączenie z internetem.
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     */
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

    /**
     * Pokazuje dialog dla dodania obrazku serwisu
     */
    @Override
    public void pickAttachment() {
        pickDialog.show();
    }

    /**
     * Wysyłanie wiadomości
     * <p>
     *   Za pomoća Singletona odpawiamy wiadomość
     * </p>
     * @param params lista z treścią wiadomości
     */
    @Override
    public void sendMessage(List<NameValuePair> params) {
        MESSAGE = getString(R.string.sending_message);
        Singleton.getSingletonInstance().setClientListener(this);
        Singleton.getSingletonInstance().sendMessage(params);
    }
    /**
     * Wywoływane, gdy aktywnośc kończy swoją działalność
     * <p>
     *     Tworzymy listę dla załączika w wiadomości,
     *     jeżeli nie jest ona pusta dodajemy je do diaalogu z wiadomościami
     * </p>
     * @param requestCode pozwala określić, skąd wynik pochodzi
     * @param  resultCode kod wyniku zwracany przez aktywność dziecka
     * @param data intent, który może zwrócić dane wynikowe
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Pair<Bitmap, String> res = pictureSelector.onActivityResult(requestCode, resultCode, data);
        if (res != null) {
            contactFragment.attachmentSelected(res);
        }
    }

    /**
     * Odswieżanie listy wiadomości
     * <p>
     *      Tworzy się lista z kluczem i wartością i jest
     *  dodawany do niej parametr id serwisu i użytkownika. Jesli Singleton jest online, wykonijemy odswieżanie,
     *  w innym przypadku pokazujemy wiadomość sprawdź połączenie z internetem i
     *  anulujemy wszelkie wizualne wskazania odświeżenia.
     * </p>
     */
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
    /**
     * Wywoływane gdy żądanie zostało wyslane
     * <p>
     *     Tworzymy i pokazujemy dialog z wskaźnikiem postępu
     *     łądowania informacji o serwisie, Wyłączamy tryb nieokreślony dla tego okna i możliwośc
     *     anulowania okna klawiszem BACK.
     * </p>
     */
    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(MESSAGE);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            /**
             * Zamknięcie bieżącego dialogu
             * @param dialog dialog, który został anulowany zostanie przekazany do metody.
             */
            @Override
            public void onCancel(DialogInterface dialog) {
                Singleton.getSingletonInstance().cancelCurrentTask();
            }
        });
        pDialog.show();
    }
    /**
     * Wywołane kiedy dane są przeanalizowane
     * <p>
     *  Usuwamy okno z ekranu i anulujemy wszelkie wizualne wskazanie odświeżenia.
     *  Jeżeli rezult == null to znaczy że są problemy z połaczeniem do internetu,
     *  W innym przypadku parsujemy ten rezult za pomocą JSONInterpretera.
     *  Jeżeli rezult jest pusty to tworzymy nową listę z wynikiem parsowania.
     *  W przeciwnym wypadku usuwamy serwisy i dodajemy nasz result do tego obiektu
     * </p>
     * @param resualt odpowiedż strony internetowej u postaci JSONobiektu
     */
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
    /**
     * Usunięcie okna z ekranu i
     *     anulowanie wszelkich wizualnych wskazań odświeżenia.
     */
    @Override
    public void onRequestCancelled() {
        if (pDialog != null)
            pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);
    }

}
