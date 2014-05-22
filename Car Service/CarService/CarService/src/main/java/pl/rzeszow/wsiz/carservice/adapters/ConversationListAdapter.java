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
 * Created by rsavk_000 on 5/22/2014.
 */
public class ConversationListAdapter extends BaseExpandableListAdapter {

    private final static String TAG = "ConversationListAdapter";

    private Context mContext;
    private ArrayList<BaseListItem> groups;

    public ConversationListAdapter(Context context) {
        this.mContext = context;
        groups = new ArrayList<BaseListItem>();
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getContactCount();
    }

    @Override
    public BaseListItem getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public BaseListItem getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getContact(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groups.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getContact(childPosition).getId();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getConvertView(getGroup(groupPosition),convertView,true);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return getConvertView(getChild(groupPosition,childPosition),convertView,false);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

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

    public void clearData(){
        groups.clear();
        notifyDataSetChanged();
    }

    public void addConversations(ArrayList<BaseListItem> data){
        groups.addAll(data);
        notifyDataSetChanged();
    }
}
