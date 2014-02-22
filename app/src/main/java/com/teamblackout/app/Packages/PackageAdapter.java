package com.teamblackout.app.Packages;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.teamblackout.app.R;

import java.util.ArrayList;

public class PackageAdapter extends ArrayAdapter<Package> {

    Context context;
    int layoutResourceId;
    private ArrayList<Package> data;
    Picasso p;

    private static final int TYPE_ITEM_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public PackageAdapter(Context context, int layoutResourceId, ArrayList<Package> data) {
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

            holder = new AppHolder();

            row = inflater.inflate(R.layout.list_icon_row, parent, false);
            holder.txtTitle = (TextView) row.findViewById(R.id.textView);
            holder.updated = (TextView) row.findViewById(R.id.dateUpdated);
            holder.imgIcon = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);
        }
        else
        {
            holder = (AppHolder)row.getTag();
        }

        final AppHolder finalHolder = holder;
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                finalHolder.imgIcon.setImageDrawable(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        };

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        switch(getItemViewType(position)) {
            case TYPE_ITEM:
                holder.txtTitle.setText(data.get(position).title);
                holder.txtTitle.setTypeface(null, Typeface.NORMAL);
                p.with(context)
                        .load(data.get(position).icon)
                        .placeholder(android.R.drawable.ic_delete)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(target);
                holder.updated.setText(data.get(position).date);
                params.setMargins(150, 0, 0 ,0);
                holder.txtTitle.setLayoutParams(params);
                holder.updated.setVisibility(View.VISIBLE);
                holder.imgIcon.setVisibility(View.VISIBLE);
                break;
            case TYPE_ITEM_HEADER:
                holder.txtTitle.setText(data.get(position).section);
                holder.txtTitle.setTypeface(null, Typeface.BOLD);
                params.setMargins(5, 5, 5, 5);
                holder.txtTitle.setLayoutParams(params);
                holder.updated.setVisibility(View.GONE);
                holder.imgIcon.setVisibility(View.GONE);
                break;
        }

        return row;
    }

    static class AppHolder
    {
        TextView txtTitle;
        ImageView imgIcon;
        TextView updated;
    }

    @Override
    public int getItemViewType(int position) {
        Package apk = data.get(position);
        if (apk.section.isEmpty()) {
            return TYPE_ITEM;
        } else {
            return TYPE_ITEM_HEADER;
        }
    }
}