package pl.rzeszow.wsiz.carservice.utils.json;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import pl.rzeszow.wsiz.carservice.utils.async.RequestMethod;

/**
 * Klasa wykonania http żądań
 * <p>
 *     Wykonanie GET i POST żądań i odczytywanie odpowiedzi w postaci JSONObject
 * </p>
 */
public class JSONRequester {

    private static InputStream is = null;       //!< Strumień do wczytywania danych
    private static JSONObject jObj = null;      //!< Zmienna do przechowywania odpowiedzi w postaci JSONObject
    private static String json = "";            //!< Zmienna do wczytawania odzpowiedzi w postaci wiersza symboli
    private static String TAG = "JSONRequester";

    public JSONRequester() { }

    /**
     * Wykonanie żądania odpowiedniego do RequestMethod
     * @param url link do wykonania żądania
     * @param method metod wykonania żądania
     * @param params parametry żądania
     * @return odpowiedź na żądanie
     */
    public static JSONObject makeHttpRequest(String url, RequestMethod method,
                                      List<NameValuePair> params) {
        // Making HTTP request
        try {
            // check for request method
            if (method == RequestMethod.GET_FROM_URL){

                // Construct the client and the HTTP request.
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                // Execute the POST request and store the response locally.
                HttpResponse httpResponse = httpClient.execute(httpPost);
                // Extract data from the response.
                HttpEntity httpEntity = httpResponse.getEntity();
                // Open an inputStream with the data content.
                is = httpEntity.getContent();

            }else if(method == RequestMethod.POST){

                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }else if(method == RequestMethod.GET){

                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

        } catch (Exception e) {
            Log.e(TAG, "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }
        // return JSON Object
        return jObj;
    }
}
