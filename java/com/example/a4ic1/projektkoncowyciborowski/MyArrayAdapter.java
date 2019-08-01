package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 4ic1 on 2016-09-16.
 */
public class MyArrayAdapter<String> extends ArrayAdapter<String> {

    String[] fileNames;
    Context ctx;

    public MyArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        fileNames = objects;
        ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //inflater - klasa konwertująca xml na kod javy
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.grid_special, null);
        //szukam kontrolki w layoucie

        TextView tv1 = (TextView) convertView.findViewById(R.id.cell_txt);
        tv1.setText(fileNames[position]+"");
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("info",position+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                ( (KolazActivity)ctx).activateFolder(position);
            }
        });
        //
        ImageView iv1 = (ImageView) convertView.findViewById(R.id.delButton);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("info",position+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                ( (KolazActivity)ctx).deleteFolder(position);
            }
        });

        return convertView;
    }
}
