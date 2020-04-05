package com.example.databaseapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactListAdapter extends BaseAdapter {

    private ArrayList<User> users;
    private Context context;

    public ContactListAdapter(Context context,ArrayList<User> users){
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).uid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.contactlist_user_item,parent,false);
        }
        User currentUser = (User) getItem(position);

        TextView Name = convertView.findViewById(R.id.listview_item_contact_name);
        TextView Number = convertView.findViewById(R.id.listview_item_contact_number);
        Name.setText(currentUser.firstName);
        Number.setText(currentUser.phone_number);
        return convertView;
    }

    public void ChangeDataSet(ArrayList<User> users){
        this.users = users;
        notifyDataSetChanged();
    }


}
