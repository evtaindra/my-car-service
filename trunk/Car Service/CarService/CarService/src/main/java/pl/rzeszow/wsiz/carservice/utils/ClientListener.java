package pl.rzeszow.wsiz.carservice.utils;

import org.json.JSONObject;

/**
 * Created by rsavk_000 on 4/13/2014.
 */
public interface ClientListener {
    public void onRequestSent();
    public void onDataReady(JSONObject resualt);
    public void onRequestCancelled();
}
