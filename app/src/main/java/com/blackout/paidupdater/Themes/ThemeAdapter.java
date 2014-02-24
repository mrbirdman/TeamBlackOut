package com.blackout.paidupdater.Themes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackout.paidupdater.R;

import java.util.ArrayList;

public class ThemeAdapter extends ArrayAdapter<ThemeList> {

    Context context;
    int layoutResourceId;
    private ArrayList<ThemeList> data;

    public ThemeAdapter(Context context, int layoutResourceId, ArrayList<ThemeList> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AppHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.list_header_row, parent, false);

            holder = new AppHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.headerView);

            row.setTag(holder);
        }
        else
        {
            holder = (AppHolder)row.getTag();
        }

        holder.txtTitle.setText(data.get(position).title);

        return row;
    }

    static class AppHolder
    {
        TextView txtTitle;
        ImageView imgIcon;
    }
}