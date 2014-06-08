package pl.rzeszow.wsiz.carservice.utils;

import org.json.JSONObject;

/**
 * Interface do wykonania callbackow
 * <p>
 *     Dostawa danych do widoku oraz reagowanie widok na rozpocięcie lub zakończenie żadania.
 * </p>
 */
public interface ClientListener {
    public void onRequestSent();
    public void onDataReady(JSONObject resualt);
    public void onRequestCancelled();
}
