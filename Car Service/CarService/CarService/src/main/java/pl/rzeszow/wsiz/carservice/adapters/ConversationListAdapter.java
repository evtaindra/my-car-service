package pl.rzeszow.wsiz.carservice.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.model.BaseListItem;
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.model.User;

/**
 * Klasa obslugująca rozmowy pomiędzy użytkownikami i serwisami
 * <p>
 *    Służy do wyświetlania rozmów w rozszerzonym widoku listy
 * </p>
 */
public class ConversationListAdapter extends BaseExpandableListAdapter {

    private final static String TAG = "ConversationListAdapter";

    private Context mContext; //!< służy do przytrzymania activity, z którym współpracuje adapter
    private ArrayList<BaseListItem> groups; //!<lista zawierająca grupy wiadomości.

    /**
     * Tworzenie nowego Adaptera
     *
     * @param context przytrzymuje activity, z którym współpracuje adapter
     */
    public ConversationListAdapter(Context context) {
        this.mContext = context;
        groups = new ArrayList<BaseListItem>();
    }

    /**
     * Wyświetla ile grup wiadomości jest w zbiorze danych
     *
     * @return liczbę, ile obiektów ta lista zawiera
     */
    @Override
    public int getGroupCount() {
        return groups.size();
    }

    /**
     * Pobiera liczbę wiadomości w tej rozszerzonej liście.
     *
     * @param groupPosition pozycja grupy wiadomości na liście
     * @return liczbę wiadomości w tej rozszerzonej liście.
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getContactCount();
    }

    /**
     * Pobiera grupę wiadomości na danej pozycji
     * @param groupPosition pozycja grupy wiadomości na liście
     * @return grupę wiadomości na zadanej pozycji
     */
    @Override
    public BaseListItem getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    /**
     * Pobiera dane związane z wiadomością w danej grupie.
     * @param groupPosition pozycja grupy wiadomości na liście
     * @param childPosition pozycja zadanej wiadomości na liście
     * @return wiadomość w danej grupie wiadomości
     */
    @Override
    public BaseListItem getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getContact(childPosition);
    }

    /**
     * Pobiera id grupę wiadomości na danej pozycji
     * @param groupPosition pozycja grupy wiadomości
     * @return id grupy wiadomości na zadanej pozycji
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groups.get(groupPosition).getId();
    }

    /**
     * Pobiera id poszczególnej wiadomości w danej grupie
     * @param groupPosition pozycja grupy wiadomości
     * @param childPosition pozycja poszczególnej wiadomości
     * @return id poszczególnej wiadomości w danej grupie
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getContact(childPosition).getId();
    }

    /**
     * Pobiera widok, który wyświetla daną grupę.
     * @param groupPosition pozycja grupy wiadomości
     * @param isExpanded czy grupa jest rozwinięta
     * @param convertView widok wyświetlający daną grupę
     * @param parent widok, który zawiera inny widok
     * @return widok, który wyświetla daną grupę.
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getConvertView(getGroup(groupPosition),convertView,true);
    }

    /**
     * Pobiera widok, który wyświetla dane dla danej wiadomości w obrębie danej grupy.
     *
     * @param groupPosition pozycja grupy wiadomości
     * @param childPosition pozycja poszczególnej wiadomości
     * @param isLastChild czy to jest ostatnia wiadomość
     * @param convertView widok wyświetlający daną grupę
     * @param parent widok, który zawiera inny widok
     * @return widok, który wyświetla dane dla danej wiadomości w obrębie danej grupy.
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return getConvertView(getChild(groupPosition,childPosition),convertView,false);
    }

    /**
     * Pokazuje czy wiadomośc w danej grupie jest wybrana
     *
     * @param groupPosition pozycja grupy wiadomości
     * @param childPosition pozycja poszczególnej wiadomości w grupie
     * @return czy wiadomośc w danej grupie zostala wybrana
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * Wskazuje, czy id pozycji są stabilne przez zmiany w danych źródłowych.
     * @return true, jeśli sam id zawsze odnosi się do tego samego obiektu.
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Pobiera widok grupy lub dziecka w zależności od grupy
     * <p>
     *  Gdy widok nic nie zawiera inicjalizujemy XML plik.
     *  Potem w zależności od typu grupy wiadomości
     *  przypisujemy jeszcze hierarchię widoku dla
     *  Serwisu lub Użytkownika.
     *  Dla Serwisu jeżeli to jest grupa wiadomości przypisujemy imię,
     *  ustawiamy layout, kolor, położenie, kolor tekstu.
     *  Dla Użytkownika wykorzystujemy te same metody.
     * </p>
     * @param item grupa wiadomości
     * @param convertView widok grupy lub dziecka
     * @param isGroup czy to jest grupa czy dziecko
     * @return widok grupy lub dziecka w zależności od grupy
     */
    private View getConvertView(BaseListItem item,View convertView,boolean isGroup){
        if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
                if (item instanceof Service)
                    convertView = inf.inflate(R.layout.service_cl_item, null);
                else if (item instanceof User)
                    convertView = inf.inflate(R.layout.user_cl_item, null);
        }
        if (item instanceof Service) {
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(((Service) item).getName());

            if(isGroup){
                LinearLayout mainLayout = (LinearLayout) convertView.findViewById(R.id.main_layout);
                mainLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_dark));
                mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                name.setTextColor(Color.WHITE);
            }
        } else if (item instanceof User) {
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView surname = (TextView) convertView.findViewById(R.id.surname);
            name.setText(((User) item).getName());
            surname.setText(((User) item).getSurname());

            if(isGroup){
                LinearLayout mainLayout = (LinearLayout) convertView.findViewById(R.id.main_layout);
                mainLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
                mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                name.setTextColor(Color.WHITE);
            }
        }
        return convertView;
    }
    /**
     *Usuwanie wszystkich elementów z listy i odswieżanie zestawu danych
     */
    public void clearData(){
        groups.clear();
        notifyDataSetChanged();
    }

    /**
     * Dodanie rozmowy do listy i odswieżanie zestawu danych
     * @param data lista zawierająca grupy wiadomości.
     */
    public void addConversations(ArrayList<BaseListItem> data){
        groups.addAll(data);
        notifyDataSetChanged();
    }
}
