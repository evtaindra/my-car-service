package pl.rzeszow.wsiz.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.fragments.ServiceListFragment;

public class Guest extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ServiceListFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            getSharedPreferences(Constants.LOGIN, Context.MODE_PRIVATE).edit().clear().commit();
            finish();
            startActivity(new Intent(Guest.this, Login.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
