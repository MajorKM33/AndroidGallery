package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MajorKM33 on 23.11.2016.
 */
public class DrawerArrayAdapter<String> extends ArrayAdapter<String> {

    String[] fileNames;
    Context ctx;

    public DrawerArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        fileNames = objects;
        ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //inflater - klasa konwertująca xml na kod javy
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_cell, null);

        TextView tv1 = (TextView) convertView.findViewById(R.id.cell_txt);
        tv1.setText(fileNames[position]+"");
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //( (KolazActivity)ctx).activateFolder(position);
            }
        });
        //
        ImageView iv1 = (ImageView) convertView.findViewById(R.id.cell_img);
        if( position == 0 ){
            iv1.setImageResource(R.drawable.fontcon);
        }
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TARGET",position+"");

                //( (KolazActivity)ctx).deleteFolder(position);
            }
        });

        return convertView;
    }
}
