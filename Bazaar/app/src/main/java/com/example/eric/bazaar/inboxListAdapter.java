package com.example.eric.bazaar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by eric on 4/8/17.
 */

public class inboxListAdapter extends BaseAdapter {
    private ArrayList<uMess> listData;
    private LayoutInflater layoutInflater;

    public inboxListAdapter(Context aContext, ArrayList<uMess> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new CustomListAdapter.ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.price);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.user);
            convertView.setTag(holder);
        } else {
            holder = (CustomListAdapter.ViewHolder) convertView.getTag();
        }

        holder.headlineView.setText(listData.get(position).getitem());
        holder.reporterNameView.setText(listData.get(position).getSender());
        holder.reportedDateView.setText(listData.get(position).gettext());
        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
    }
}
