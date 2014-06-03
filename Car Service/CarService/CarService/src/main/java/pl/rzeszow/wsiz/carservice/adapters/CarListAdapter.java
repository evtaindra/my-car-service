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
 * Klasa obslugująca listę samochodów
 * <p>
 *    Służy do wyświetlania danych o samochodzie na liście
 * </p>
 */
public class CarListAdapter extends BaseAdapter {

    private ArrayList<Car> carList; //!< lista zawierająca samochody.
    private Context mContext;       //!< służy do przytrzymania activity, z którym współpracuje adapter

    /**
     * Tworzenie nowego Adaptera
     *
     * @param context przytrzymuje activity, z którym współpracuje adapter
     */
    public CarListAdapter(Context context){
        this.carList = new ArrayList<Car>();
        mContext = context;
    }

    /**
     * Dodanie samochodów do listy i odswieżanie zestawu danych
     *
     * @param cars lista zawierająca samochody
     */
    public void addCars(ArrayList<Car> cars){
        this.carList.addAll(cars);
        notifyDataSetChanged();
    }

    /**
     *Usuwanie wszystkich elementów z listy i odswieżanie zestawu danych
     */
    public void clearData(){
        this.carList.clear();
        notifyDataSetChanged();
    }

    /**
     * Wyświetla ile samochodów jest w zbiorze danych
     *
     * @return liczbę, ile obiektów ta lista zawiera
     */
    @Override
    public int getCount() {
        return carList.size();
    }

    /**
     * Zwraca samochód na danej pozycji
    * @param position pozycja elementa w zbiorze danych
    * @return element danych, jaki jest powiązany z określoną pozycją w zbiorze danych
    */
    @Override
    public Car getItem(int position) {
        return this.carList.get(position);
    }

    /**
     * Zwraca id samochodu na danej pozycji
     * @param position pozycja elementa w zbiorze danych
     * @return id elementa związany z określoną pozycję na liście.
     */
    @Override
    public long getItemId(int position) {
        return getItem(position).getNr_id();
    }

    /**
     * Pobieranie widoku, który wyświetla dane w określonej pozycji w zbiorze danych.
     * <p>
     * Gdy widok nic nie zawiera inicjalizujemy XML plik.
     * Przypisujemy dane o samochodzie do odpowiednich pół.
     * </p>
     * @param position pozycja elementa w zbiorze danych
     * @param convertView nowy widok
     * @param parent widok, który zawiera inny widok
     * @return widok  wyświetlający dane w określonej pozycji.
     */
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
                TextView carRok = (TextView) view.findViewById(R.id.rok);

                carName.setText(item.getMarka());
                carModel.setText(item.getModel());
                carRej.setText(item.getNr_rej());
                carRok.setText(Integer.toString(item.getRok()));
        }
        return view;
    }
}
