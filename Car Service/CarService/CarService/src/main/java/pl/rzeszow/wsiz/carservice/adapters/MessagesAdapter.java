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
import pl.rzeszow.wsiz.carservice.model.Message;

/**
 * Created by rsavk_000 on 5/24/2014.
 */
public class MessagesAdapter extends BaseAdapter {

    private ArrayList<Message> messageList;
    private Context mContext;
    private int sender;

    public MessagesAdapter(Context context, int sender) {
        this.messageList = new ArrayList<Message>();
        this.sender = sender;
        mContext = context;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Message getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.message_item, null);
        }
        Message item = getItem(position);
        if (item != null) {
            TextView date = (TextView) view.findViewById(R.id.date);
            TextView content = (TextView) view.findViewById(R.id.content);
            ImageView attachment = (ImageView) view.findViewById(R.id.attachment);

            date.setText(item.getDate());
            content.setText(item.getContent());
            attachment.setImageBitmap(item.getAttachment());
        }
        return view;
    }
    public void addServices(ArrayList<Message> messages){
        this.messageList.addAll(messages);
        notifyDataSetChanged();
    }
    public void clearData(){
        this.messageList.clear();
        notifyDataSetChanged();
    }
}
