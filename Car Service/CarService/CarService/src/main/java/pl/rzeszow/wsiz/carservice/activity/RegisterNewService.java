package pl.rzeszow.wsiz.carservice.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.image.BitmapEnDecode;
import pl.rzeszow.wsiz.carservice.utils.image.PictureSelector;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Klasa RegisterNewServise
 * <p>
 *   Służy do rejestracji serwisu
 * </p>
 */

public class RegisterNewService extends Activity implements View.OnClickListener, ClientListener {

    private AlertDialog imageDialog; //!< Dialog dla dodania obrazek serwisu
    private ProgressDialog pDialog; //!< Dialog z wskaźnikiem postępu rejestracji serwisu

    private ImageView imageView; //!< służy do wyświetlania obrazku
    private Bitmap image; //!< służy do wyświetlania obrazku

    private EditText sName, sCity, sAddress, sDescription; //!< pola, w których tekst może być edytowany
    private Button mRegister; //!< przycisk rejestracji serwisu
    private PictureSelector pictureSelector; //!< służy do wybrania obrazku
    private String TAG = "RegisterNewService"; //!< zmienna przyjmująca wartość string

    /**
     *  Wywoływane, gdy aktywność zaczyna.
     *  <p>
     *       Ustawienie treści do widoku, tekstedytorów, dialogu do wybrania obrazku.
     *       Ustawienie akcji która będzie wykonywana przy nacisku na przycisk.
     *  </p>
     * @param savedInstanceState  Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_service);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(this);

        pictureSelector = new PictureSelector(this);
        imageDialog = pictureSelector.buildImageDialog();

        sName = (EditText) findViewById(R.id.name);
        sCity = (EditText) findViewById(R.id.scity);
        sAddress = (EditText) findViewById(R.id.saddress);
        sDescription = (EditText) findViewById(R.id.sdescription);

        mRegister = (Button) findViewById(R.id.sregister);
        mRegister.setOnClickListener(this);
    }

    /**
     * Wywoływane, gdy widok został kliknięty.
     * <p>
     *     Jeżeli został kliknięty na bitmap to pokazuje się dialog z wybraniem obrazku.
     *     Jeżeli na rejestrację serwisu to będzie konwertowane w string to co zostało wpisane w polach,
     *     gdy pola są puste, będzie pokazywany błąd na tym polu,
     *     jakie jest puste. W przeciwnym razie tworzy się lista z
     *     kluczem i wartością i są dodawane do niej parametry z tekstedytorów.
     *     Za pomocą Singletona wykonijemy akcję.
     * </p>
     * @param v widok, który został kliknięty.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.image) {
            imageDialog.show();
        } else if (id == R.id.sregister) {
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
                params.add(new BasicNameValuePair("sname", name));
                params.add(new BasicNameValuePair("scity", city));
                params.add(new BasicNameValuePair("saddress", address));
                params.add(new BasicNameValuePair("sdescription", description));
                params.add(new BasicNameValuePair("simage", BitmapEnDecode.BitmapToString(image)));
                params.add(new BasicNameValuePair("suserid", String.valueOf(Singleton.getSingletonInstance().userID)));

                //always don`t forget set client
                Singleton.getSingletonInstance().setClientListener(this);
                Singleton.getSingletonInstance().createNewService(params);
            }
        }
    }

    /**
     * Wywoływane, gdy aktywnośc kończy swoją działalność
     * <p>
     *     Przypisuje ten obrazek, który wybraliśmy do bitmapy
     * </p>
     * @param requestCode  pozwala określić, skąd wynik pochodzi.
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
     *   Pokazywanie okna z dialogiem i wskaźnikiem postępu tworzenia serwisu
     * <p>
     *     Wyłączenie trybu nieokreślonego dla tego okna
     *     Możliwośc okna anulować klawiszem BACK.
     */
    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(RegisterNewService.this);
        pDialog.setMessage("Creating Service...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Wywołane kiedy dane są przeanalizowane
     * <p>
     *  Usuwamy okno z ekranu. Parsujemy ten rezult za pomocą JSONInterpretera
     *  Jeżeli w rezult integer==1 to znaczy że serwis został dodany,
     *  W przeciwnym wypadku serwis nie został dodany. I pokazujemy o tym wiadomość
     * </p>
     * @param resualt odpowiedż strony internetowej u postaci JSONobiektu
     */
    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();

        Pair<Integer, String> ires = JSONInterpreter.parseMessage(resualt);

        String message = ires.second;

        if (ires.first == 1) {
            Log.d(TAG, "Service created");
            finish();

        } else {
            Log.d(TAG, "Failed to create a service!");
        }

        if (message != null) {
            Toast.makeText(RegisterNewService.this, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     *  Usunięcie okna z ekranu.
     */
    @Override
    public void onRequestCancelled() {
        pDialog.dismiss();
    }
}
