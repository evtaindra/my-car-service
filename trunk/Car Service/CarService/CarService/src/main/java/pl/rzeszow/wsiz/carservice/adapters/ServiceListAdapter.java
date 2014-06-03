package pl.rzeszow.wsiz.carservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.model.Service;

/**
 * Klasa obslugująca listę serwisow
 * <p>
 *    Służy do wyświetlania danych o serwisie na liście
 * </p>
 */
public class ServiceListAdapter extends BaseAdapter {

    private ArrayList<Service> serviceList;//!< lista zawierająca serwisy.
    private Context mContext;          //!< służy do przytrzymania activity, z którym współpracuje adapter

    /**
     * Tworzenie nowego Adaptera
     *
     * @param context przytrzymuje activity, z którym współpracuje adapter
     */
    public ServiceListAdapter(Context context) {
        this.serviceList = new ArrayList<Service>();
        mContext = context;
    }

    /**
     * Dodanie serwisów do listy i odswieżanie zestawu danych
     *
     * @param services lista zawierająca serwisy
     */
    public void addServices(ArrayList<Service> services){
        this.serviceList.addAll(services);
        notifyDataSetChanged();
    }
    /**
     * Usuwanie wszystkich elementów z listy i odswieżanie zestawu danych
     */
    public void clearData(){
        this.serviceList.clear();
        notifyDataSetChanged();
    }
    /**
     * Wyświetla ile serwisów jest w zbiorze danych
     *
     * @return liczbę, ile obiektów ta lista zawiera
     */
    @Override
    public int getCount() {
        return serviceList.size();
    }
    /**
     * Zwraca serwis na danej pozycji
     * @param position pozycja elementa w zbiorze danych
     * @return element danych, jaki jest powiązany z określoną pozycją w zbiorze danych
     */
    @Override
    public Service getItem(int position) {
        return this.serviceList.get(position);
    }
    /**
     * Zwraca id serwisu na danej pozycji
     * @param position pozycja elementa w zbiorze danych
     * @return id elementa związany z określoną pozycję na liście.
     */
    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    /**
     * Pobieranie serwisu po jego id
     * <p>
     *  Jeżeli takie id serwisu istneje pośród wszystkich serwisów
     *  to pobieramy ten serwis
     * </p>
     * @param id id serwisu
     * @return serwis z szukanym id
     */
    public Service getItemById(long id){
        for (int i = 0; i < serviceList.size(); i++) {
            if( id == serviceList.get(i).getId()) {
                return serviceList.get(i);
            }
        }
        return null;
    }

    /**
     * Aktualizacja rankingu po id serwisu i odswieżanie zestawu danych
     * @param id id serwisu, ranking którego aktualizujemy
     * @param rating ocena serwisu
     */
    public void updateItemRating(long id, double rating){
        getItemById(id).setRating(rating);
        notifyDataSetChanged();
    }

    /**
     * Pobieranie widoku, który wyświetla dane w określonej pozycji w zbiorze danych.
     * <p>
     * Gdy widok nic nie zawiera inicjalizujemy XML plik.
     * Przypisujemy dane o serwisie do odpowiednich pół.
     * </p>
     *
     * @param position pozycja elementa w zbiorze danych
     * @param convertView nowy widok
     * @param parent widok, który zawiera inny widok
     * @return widok  wyświetlający dane w określonej pozycji.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.service_item, null);
        }
        Service item = getItem(position);
        if (item != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.serviceName);
            TextView serviceRating = (TextView) view.findViewById(R.id.serviceRating);
            TextView serviceCity = (TextView) view.findViewById(R.id.serviceCity);
            TextView serviceAddress = (TextView) view.findViewById(R.id.serviceAddress);

            ImageView serviceImage = (ImageView) view.findViewById(R.id.serviceImage);

            serviceName.setText(item.getName());
            serviceRating.setText(String.valueOf(item.getRating()));
            serviceCity.setText(item.getCity());
            serviceAddress.setText(item.getAddress());

            serviceImage.setImageBitmap(item.getImage());
        }
        return view;
    }
}
