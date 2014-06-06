package pl.rzeszow.wsiz.carservice.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.fragments.SendMessageFragment;
import pl.rzeszow.wsiz.carservice.fragments.ServiceListFragment;
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.image.PictureSelector;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Klasa ServiceDetail
 * <p>
 *   Służy wyświetlenia i modyfikacji szczególnej informacji o serwisie
 * </p>
 */
public class ServiceDetail extends ActionBarActivity implements
        ClientListener,
        RatingBar.OnRatingBarChangeListener,
        SendMessageFragment.FragmentCallBack {

    private final String TAG = "ServiceDetail"; //!< zmienna przyjmująca wartość string

    private long serviceID; //!< id serwisu

    private ImageView serviceImage;//!< służy do wyświetlania obrazku
    private TextView serviceName, serviceDescription, serviceCity, serviceAddress, servicePhone, serviceEmail; //!< pola, w których tekst może być edytowany
    private RatingBar serviceRating; //!< pokazuje oceny serwisu w gwiazdach
    private Service mService; //!< obiekt serwisu.
    private boolean isRatingLoaded;//!< czy ocena jest załadowana

    private ProgressDialog pDialog;  //!< dialog z wskaźnikiem postępu wyświetlenia szczeglnej informacji o serwisie

    private SendMessageFragment contactDialog;//fragment dialogu pomiędzy użytkownikiem a serwisem

    private AlertDialog pickDialog; //!< Dialog dla dodania obrazku serwisu
    private PictureSelector pictureSelector; //!< służy do wybrania obrazku
    private String MESSAGE;  //!< zmienna przyjmująca wartość string ("Loading service details")

    /**
     *  Ustawienie treści do widoku. Jeżeli intencję, która rozpoczęła tę działalność
     *  nie jest pusta pobieramy id serwisu. Ustawiamy  tekstedytory do widoku i listener dla oceny serwisu.
     *  Tworzy się lista z kluczem i wartością i jest
     *  dodawany do niej parametr id serwisu. Wykonijemy akcję za pomocą Singleton. Potem dodajemy listener na
     *  wybieranie obrazku.
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        if (getIntent() != null)
            serviceID = getIntent().getExtras().getLong(Constants.SERVICE_ID);

        serviceImage = (ImageView) findViewById(R.id.serviceImage);
        serviceName = (TextView) findViewById(R.id.serviceName);
        serviceDescription = (TextView) findViewById(R.id.serviceDescription);
        serviceCity = (TextView) findViewById(R.id.serviceCity);
        serviceAddress = (TextView) findViewById(R.id.serviceAddress);
        servicePhone = (TextView) findViewById(R.id.servicePhone);
        serviceEmail = (TextView) findViewById(R.id.serviceEmail);
        serviceRating = (RatingBar) findViewById(R.id.serviceRating);

        serviceRating.setOnRatingBarChangeListener(this);
        isRatingLoaded = false;

        Singleton.getSingletonInstance().setClientListener(this);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        MESSAGE = getString(R.string.loading_service_details);
        params.add(new BasicNameValuePair("sr_id", Long.toString(serviceID)));
        Singleton.getSingletonInstance().getServiceDetails(params);

        pictureSelector = new PictureSelector(this);
        pickDialog = pictureSelector.buildImageDialog();
    }

    /**
     * Powiadomienie, że ocena została zmieniona.
     * <p>
     *     Jeżeli ocena została zmieniona, tworzy się lista z kluczem i wartością i jest
     *  dodawany do niej parametry id serwisu, którego oceniamy, ocenę i id użytkownika, który
     *  wystawił tą ocenę.  Wykonijemy akcję za pomocą Singleton.Ustawiamy załadowanie oceny na false.
     * </p>
     * @param ratingBar ratingBar, gdzie ocena została zmieniona
     * @param rating aktualna ocena
     * @param fromUser true, jeśli ocena została zainicjowana przez dotykowy gest użytkownika lub klawisz
     */
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (isRatingLoaded) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().userID)));
            params.add(new BasicNameValuePair("sr_id", Integer.toString(mService.getId())));
            params.add(new BasicNameValuePair("mark", Float.toString(rating)));

            MESSAGE = getString(R.string.rating_service);
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().rateService(params);
            isRatingLoaded = false;
        }
    }

    /**
     * Inicjalizacja zawartości menu.
     * <p>
     * Jeżeli id Użytkownika nie jest równe 0,tworzymy wystąpienia XML plików w menu objektach i
     * i tworzymy hierarchię menu z określonego XML zasobu.
     * </p>
     * @param menu menu, w którym można umieścić Opcje.
     * @return true dla wyświetlenia menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Singleton.getSingletonInstance().userID != 0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_details, menu);
        }
        return true;
    }

    /**
     *  Jest wywoływana, kiedy został wybrany element z menu
     * <p>
     *     Pobieramy id tego elementa, jeżeli to jest napisania wiadomości i
     *     użytkownik próbuje komunikować z własnym serwisem wystąpi błąd.
     *     W innym przypadku mapujemy z wartości string do int i bool parametry,
     *     otwieramy kontaktny dialog
     * </p>
     * @param item element menu, który został wybrany.
     * @return false aby umożliwić normalne menu dla kontynuacji przetwarzania,
     * true aby je konsumować.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_contact) {
            if (Singleton.getSingletonInstance().userID == mService.getUs_id()) {
                Toast.makeText(this, getString(R.string.contact_not_allowed), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Bundle arg = new Bundle();
                arg.putInt("userID", Singleton.getSingletonInstance().userID);
                arg.putInt("serviceID", mService.getId());
                arg.putInt("sender", 1);
                arg.putBoolean("isDialog",true);
                contactDialog = new SendMessageFragment();
                contactDialog.setArguments(arg);
                contactDialog.show(getSupportFragmentManager(), null);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
        pDialog.show();
    }

    /**
     * Wywołane kiedy dane są przeanalizowane
     * <p>
     *  Usuwamy okno z ekranu. Jeżeli result jest pusty będzie błąd z
     *  połączeniem do internetu. W innym przypadku
     *  parsujemy ten rezult za pomocą JSONInterpretera.
     *  Jeżeli rezult nie jest null ustawiamy dane o serwisie w odpowiedne pola,
     *  W innym przypadku gdy użytkownik chciał aktualizować ocenę serwisu,
     *  parsujemy tą ocenę i aktualizujemy danę. Gdy użytkownik chciał wysłac
     *  wiadomość będzie wyswietlono że udało sie. Jeszcze innym wypadku wystąpi błąd.
     * </p>
     * @param resualt odpowiedż strony internetowej u postaci JSONobiektu
     */
    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
        if (resualt == null) {
            Toast.makeText(this, getString(R.string.alert_connection_problem), Toast.LENGTH_LONG).show();
        } else {
            Service tmp = JSONInterpreter.parseService(resualt, true);
            if (tmp != null) {
                mService = tmp;
                setServiceData();
            } else {
                Pair<Integer, String> ires = JSONInterpreter.parseMessage(resualt);
                if (ires.first == 1) {
                    Log.d(TAG, "Rate Successful!");
                    mService.setRating(Float.parseFloat(ires.second));
                    serviceRating.setRating(Float.parseFloat(ires.second));
                    ServiceListFragment.updateServiceRating(mService.getId(), Double.parseDouble(ires.second));
                    Toast.makeText(this, getString(R.string.rate_success), Toast.LENGTH_SHORT).show();
                } else if (ires.first == 2) {
                    Log.d(TAG, "Send Successful");
                    Toast.makeText(this, ires.second, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, getString(R.string.op_failed), Toast.LENGTH_LONG).show();
                }
                isRatingLoaded = true;
            }
        }
    }

    /**
     *  Usunięcie okna z ekranu.
     */
    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }

    /**
     * Ustawienie w odpowiednie pola informacji o wybranym serwisie i
     * gdy nie istneje użytkownika lub to jest jego własny serwis postawić ocenę, to nie będzie widoczny
     * mu aktualizacja rankingu
     */
    private void setServiceData() {
        serviceImage.setImageBitmap(mService.getImage());
        serviceName.setText(mService.getName());
        serviceRating.setRating((float) mService.getRating());
        isRatingLoaded = true;
        serviceDescription.setText(mService.getDescription());
        serviceCity.setText(mService.getCity());
        serviceAddress.setText(mService.getAddress());
        servicePhone.setText(mService.getPhone());
        serviceEmail.setText(mService.getEmail());

        if (Singleton.getSingletonInstance().userID == mService.getUs_id() ||
                Singleton.getSingletonInstance().userID == 0) {
            serviceRating.setEnabled(false);
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Pair<Bitmap, String> res = pictureSelector.onActivityResult(requestCode, resultCode, data);
        if (res != null) {
            contactDialog.attachmentSelected(res);
        }
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
}
