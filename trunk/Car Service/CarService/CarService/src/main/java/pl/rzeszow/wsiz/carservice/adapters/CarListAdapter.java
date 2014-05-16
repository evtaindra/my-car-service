package pl.rzeszow.wsiz.carservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.model.Car;

/**
 * Created by opryima on 2014-05-16.
 */
public class CarListAdapter extends BaseAdapter {

    private ArrayList<Car> carList;
    private Context mContext;

    public CarListAdapter(Context context){
        this.carList = new ArrayList<Car>();
        mContext = context;
    }

    public void addCars(ArrayList<Car> cars){
        this.carList.addAll(cars);
        notifyDataSetChanged();
    }

    public void clearData(){
        this.carList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public Car getItem(int position) {
        return this.carList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getNr_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.car_item, null);
        }
        Car item = getItem(position);

        if(item != null){
            TextView carName = (TextView) view.findViewById(R.id.marka);
            TextView carModel = (TextView) view.findViewById(R.id.model);
            TextView carRej = (TextView) view.findViewById(R.id.nrRej);
            TextView carSilnik = (TextView) view.findViewById(R.id.silnik);
            TextView carPrzebieg = (TextView) view.findViewById(R.id.przebieg);
            TextView carKolor = (TextView) view.findViewById(R.id.kolor);
            TextView carPaliwo = (TextView) view.findViewById(R.id.paliwo);
            TextView carRok = (TextView) view.findViewById(R.id.rok);

            /*carName.setText(item.getMarka());
            carModel.setText(item.getModel());
            carRej.setText(item.getNr_rej());
            carSilnik.setText(Float.toString(item.getSilnik()));
            carPrzebieg.setText(Integer.toString(item.getPrzebieg()));
            carKolor.setText(item.getKolor());
            carPaliwo.setText(item.getPaliwo());
            carRok.setText(Integer.toString(item.getRok()));*/
        }
        return view;
    }
}
