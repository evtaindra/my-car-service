package pl.rzeszow.wsiz.carservice.activity;

import android.app.Activity;
import android.os.Bundle;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;

/**
 * Created by rsavk_000 on 5/2/2014.
 */
public class ServiceDetail extends Activity {
    private long serviceID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        if(getIntent() != null)
            serviceID  =  getIntent().getExtras().getLong(Constants.SERVICE_ID);

    }
}
