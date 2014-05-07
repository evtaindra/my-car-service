package pl.rzeszow.wsiz.carservice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_contact){
            //TODO send message
            //by dialog ? or by activity?
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
