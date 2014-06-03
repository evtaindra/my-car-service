package pl.rzeszow.wsiz.carservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.model.Message;

/**
 * Klasa obslugująca wiadomości
 * <p>
 *    Służy do wyświetlania wiadomości na liście
 * </p>
 */
public class MessagesAdapter extends BaseAdapter {

    private ArrayList<Message> messageList; //!<lista zawierająca wiadomości.
    private Context mContext; //!< służy do przytrzymania activity, z którym współpracuje adapter
    private int sender; //!< od kogo jest przesłana wiadomość od użytkownika lub serwisu.

    /**
     * Tworzenie nowego Adaptera
     * @param context przytrzymuje activity, z którym współpracuje adapter
     * @param sender od kogo jest przesłana wiadomość od użytkownika lub serwisu.
     */
    public MessagesAdapter(Context context, int sender) {
        this.messageList = new ArrayList<Message>();
        this.sender = sender;
        mContext = context;
    }
    /**
     * Wyświetla ile wiadomości jest w zbiorze danych
     *
     * @return liczbę, ile obiektów ta lista zawiera
     */
    @Override
    public int getCount() {
        return messageList.size();
    }

    /**
     * Pobiera wiadomość na danej pozycji
     * @param position pozycja elementa w zbiorze danych
     * @return element danych, jaki jest powiązany z określoną pozycją w zbiorze danych
     */
    @Override
    public Message getItem(int position) {
        return messageList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Pobieranie widoku, który wyświetla dane w określonej pozycji w zbiorze danych.
     * <p>
     * Gdy widok nic nie zawiera inicjalizujemy XML plik.
     * Przypisujemy dane o wiadomości do odpowiednich pół.
     * Przypisujemy te parametry w pojedynczy rzęd,
     * i w zależności od kogo jest ta wiadomości ustawiamy odpowiednie marginesy
     * </p>
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

            LinearLayout message = (LinearLayout) view.findViewById(R.id.message);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (item.getSender() == sender)
                layoutParams.setMargins(0, 0, 10, 0);
            else
                layoutParams.setMargins(10, 0, 0, 0);

            message.setLayoutParams(layoutParams);
        }
        return view;
    }

    /**
     *  Dodanie wiadomości do listy i odswieżanie zestawu danych
     *
     * @param messages lista zawierająca wiadomości
     */
    public void addServices(ArrayList<Message> messages) {
        this.messageList.addAll(messages);
        notifyDataSetChanged();
    }

    /**
     *Usuwanie wszystkich elementów z listy i odswieżanie zestawu danych
     */
    public void clearData() {
        this.messageList.clear();
        notifyDataSetChanged();
    }
}
