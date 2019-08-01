package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.File;

public class AlbumActivity extends AppCompatActivity {

    GridView grid;
    ArrayAdapter<String> adapter;
    String[] fileNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        getSupportActionBar().hide();

        File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File location = new File(pic, "Ciborowski");
        File[] files = location.listFiles();
        fileNames = new String[files.length];

        for( int i = 0; i < files.length; i++ )
        {
            fileNames[i] = files[i].getName();
        }

        grid = (GridView) findViewById(R.id.grid);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                AlbumActivity.this,     // Context
                R.layout.my_cell,     // nazwa pliku xml naszej komórki
                R.id.cell_txt,         // id pola txt w komórce
                fileNames );         // tablica przechowująca dane

        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TARGET", fileNames[i]);
                Intent intent = new Intent(AlbumActivity.this,PicturesActivity.class);
                intent.putExtra("folder", fileNames[i]);
                startActivity(intent);
            }
        });

    }



}
