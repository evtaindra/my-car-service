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
 * Created by rsavk_000 on 5/2/2014.
 */
public class ServiceListAdapter extends BaseAdapter {

    private ArrayList<Service> serviceList;
    private Context mContext;

    public ServiceListAdapter(Context context) {
        this.serviceList = new ArrayList<Service>();
        mContext = context;
    }
    public void addServices(ArrayList<Service> services){
        this.serviceList.addAll(services);
        notifyDataSetChanged();
    }
    public void clearData(){
        this.serviceList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Service getItem(int position) {
        return this.serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public Service getItemById(long id){
        for (int i = 0; i < serviceList.size(); i++) {
            if( id == serviceList.get(i).getId()) {
                return serviceList.get(i);
            }
        }
        return null;
    }

    public void updateItemRating(long id, double rating){
        getItemById(id).setRating(rating);
        notifyDataSetChanged();
    }

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
