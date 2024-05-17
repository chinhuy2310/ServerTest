package com.example.servertest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.servertest.R;
import com.example.servertest.model.ItemData;

import java.util.List;

public class spinnerAdapter extends ArrayAdapter<ItemData> {
    private Context context;
    private List<ItemData> itemList;

    public spinnerAdapter(Context context, List<ItemData> itemList) {
        super(context, 0, itemList);
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        ItemData item = itemList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageViewSpinner);
        TextView textViewName = convertView.findViewById(R.id.textViewNameSpinner);

        imageView.setImageResource(item.getImageResId());
        textViewName.setText(item.getItemgridName());

        return convertView;
    }
}