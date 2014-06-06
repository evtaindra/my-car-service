package pl.rzeszow.wsiz.carservice.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.image.BitmapEnDecode;
import pl.rzeszow.wsiz.carservice.utils.image.PictureSelector;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Klasa ServiceEdit
 * <p>
 *   Służy modyfikacji szczególnej informacji o serwisie
 * </p>
 */
public class ServiceEdit extends ActionBarActivity implements ClientListener {

    Service sService; //!< obiekt serwisu.

    private AlertDialog imageDialog;  //!< Dialog dla dodania obrazek serwisu
    private ProgressDialog pDialog; //!< Dialog z wskaźnikiem postępu rejestracji serwisu

    private ImageView imageView; //!< służy do wyświetlania obrazku
    private Bitmap image; //!< służy do wyświetlania obrazku

    private EditText sName, sCity, sAddress, sDescription; //!< pola, w których tekst może być edytowany
    private Button sSave, sDelete; //!< przyciski aktualizacji i usuwania serwisu
    private PictureSelector pictureSelector; //!< służy do wybrania obrazku
    private String TAG = "ServiceEdit"; //!< zmienna przyjmująca wartość string

    private long serviceID;  //!< id serwisu

    private String MESSAGE; //!< zmienna przyjmująca wartość string ("Loading service info")

    /**
     * Wywoływane, gdy aktywność zaczyna.
     * <p>
     *      Ustawienie treści do widoku. Jeżeli intencję, która rozpoczęła tę działalność
     *  nie jest pusta pobieramy id serwisu. Tworzy się lista z kluczem i wartością i jest
     *  dodawany do niej parametr id serwisu. Jeżeli Singletone jest online wykonijemy akcję, w
     *  przeciwnym wypadku pokazujemy wiadomość sprawdź połączenie z internetem. Potem dodajemy listener na
     *  obrazek.
     * </p>
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        if (getIntent() != null)
            serviceID = getIntent().getExtras().getLong(Constants.SERVICE_ID);

        MESSAGE = getString(R.string.loading_service_info);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sr_id", Long.toString(serviceID)));

        if (Singleton.isOnline(this)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getServiceDetails(params);
        } else {
            Toast.makeText(this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
        }

        imageView = (ImageView) findViewById(R.id.simage);
        imageView.setOnClickListener(new View.OnClickListener() {
            /**
             * Wybranie obrazku
             * <p>
             *     Pokazywanie dialogu dla wybrania obrazku,
             *     Ustawiamy  tekstedytory do widoku i akcji która będzie
             *  wykonywana przy nacisku na przycisk.
             * </p>
             * @param v widok, który został kliknięty
             */
            @Override
            public void onClick(View v) {
                imageDialog.show();
            }
        });

        pictureSelector = new PictureSelector(this);
        imageDialog = pictureSelector.buildImageDialog();

        sName = (EditText) findViewById(R.id.name);
        sCity = (EditText) findViewById(R.id.scity);
        sAddress = (EditText) findViewById(R.id.saddress);
        sDescription = (EditText) findViewById(R.id.sdescription);

        sSave = (Button) findViewById(R.id.ssave);
        sSave.setOnClickListener(new View.OnClickListener() {
            /**
             * Modyfikacja serwisu
             * <p>
             *     Konwertuje w string to co zostało wpisane w polach,
             * jeżeli pola są puste, będzie pokazywany błąd na tym polu,
             * jakie jest puste. W przeciwnym razie tworzy się lista z
             * kluczem i wartością i są dodawane do niej parametry z tekstedytorów.
             *  Jeżeli Singleton jest online wykonijemy akcję, w
             *  przeciwnym wypadku pokazujemy wiadomość sprawdź połączenie z internetem.
             * </p>
             * @param v  widok, który został kliknięty
             */
            @Override
            public void onClick(View v) {
                String name = String.valueOf(sName.getText());
                String city = String.valueOf(sCity.getText());
                String address = String.valueOf(sAddress.getText());
                String description = String.valueOf(sDescription.getText());

                if (name.equalsIgnoreCase("")) {
                    showError(sName, R.string.name);
                } else if (city.equalsIgnoreCase("")) {
                    showError(sCity, R.string.city);
                } else if (address.equalsIgnoreCase("")) {
                    showError(sAddress, R.string.address);
                } else if (description.equalsIgnoreCase("")) {
                    showError(sDescription, R.string.description);
                } else {

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("sr_id", String.valueOf(serviceID)));
                    params.add(new BasicNameValuePair("name", name));
                    params.add(new BasicNameValuePair("city", city));
                    params.add(new BasicNameValuePair("adress", address));
                    params.add(new BasicNameValuePair("opis", description));
                    params.add(new BasicNameValuePair("image", BitmapEnDecode.BitmapToString(image)));

                    //always don`t forget set client
                    Singleton.getSingletonInstance().setClientListener(ServiceEdit.this);
                    Singleton.getSingletonInstance().updateServiceInfo(params);
                }
            }
        });

        sDelete = (Button)findViewById(R.id.sdelete);
        sDelete.setOnClickListener(new View.OnClickListener() {
            /**
             * Usunięcie serwisu
             * <p>
             *     Tworzy się nowa lista z kluczem i wartością i jest dodawany do niej parametr
             *  id serwisu. Jeżeli Singleton jest online wykonijemy akcję, w
             *  przeciwnym wypadku pokazujemy wiadomość sprawdź połączenie z internetem.
             * </p>
             * @param v widok, który został kliknięty
             */
            @Override
            public void onClick(View v) {
                MESSAGE = getString(R.string.deleting_car);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sr_id", Long.toString(serviceID)));
                if (Singleton.isOnline(ServiceEdit.this)) {
                    Singleton.getSingletonInstance().setClientListener(ServiceEdit.this);
                    Singleton.getSingletonInstance().deleteService(params);
                } else {
                    Toast.makeText(ServiceEdit.this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Wywoływane, gdy aktywnośc kończy swoją działalność
     * <p>
     *     Przypisuje ten obrazek, który wybraliśmy do bitmapy
     * </p>
     * @param requestCode pozwala określić, skąd wynik pochodzi.
     * @param resultCode kod wyniku zwracany przez aktywność dziecka
     * @param data intent, który może zwrócić dane wynikowe
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm = pictureSelector.onActivityResult(requestCode, resultCode, data).first;
        if (bm != null) {
            image = bm;
            imageView.setImageBitmap(bm);
        }
    }

    /**
     * Pokazywanie błądu i ustawienie fokusu na puste pole
     * @param editText pole, które jest puste
     * @param id id pola
     */
    private void showError(EditText editText, int id) {
        editText.setError(getResources().getString(R.string.error)
                + getResources().getString(id).toLowerCase());
        editText.requestFocus();
    }

    /**
     * Pokazywanie okna z dialogiem i wskaźnikiem postępu łądowania info o serwisie
     * <p>
     *     Wyłączenie trybu nieokreślonego dla tego okna
     *     Możliwośc okna anulować klawiszem BACK.
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
     *  Usunięcie okna z ekranu.
     */
    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }

    /**
     * Wywołane kiedy dane są przeanalizowane
     * <p>
     *  Usuwamy okno dialogu z ekranu. Jeżeli result nie jest null ładujemy
     *  informacje o serwisie, w innym przypadku parsujemy ten rezult za pomocą JSONInterpretera
     *  Jeżeli w rezult integer == 1 to znaczy że serwis został aktualizowany,
     *  W przeciwnym wypadku serwis nie został aktualizowany. I pokazujemy o tym wiadomość
     * </p>
     * @param resualt odpowiedż strony internetowej u postaci JSONobiektu
     */
    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
        Service tmp = JSONInterpreter.parseService(resualt, true);
        if (tmp != null) {
            sService = tmp;
            setServiceInfo();
        }else{
            Pair<Integer, String> ires = JSONInterpreter.parseMessage(resualt);

            String message = ires.second;

            if (ires.first == 1) {
                Log.d(TAG, "Service updated");
                finish();

            } else {
                Log.d(TAG, "Failed to update a service!");
            }

            if (message != null) {
                Toast.makeText(ServiceEdit.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Ustawienie w odpowiednie pola informacji o wybranym serwisie
     */
    private void setServiceInfo(){
        sName.setText(sService.getName());
        sCity.setText(sService.getCity());
        sAddress.setText(sService.getAddress());
        sDescription.setText(sService.getDescription());

        if(sService.getImage() != null)
            imageView.setImageBitmap(sService.getImage());
    }
}
