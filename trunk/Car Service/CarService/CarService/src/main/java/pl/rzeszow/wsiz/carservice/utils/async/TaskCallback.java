package pl.rzeszow.wsiz.carservice.utils.async;

import org.json.JSONObject;

/**
 * Interface do wykonania callbackow
 * <p>
 *     Dostawa danych do Singleton`u
 * </p>
 */
public interface TaskCallback {
    public void onPreExecute();
    public void onPostExecute(JSONObject result);
    public void onCancelled();
}
