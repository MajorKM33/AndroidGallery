package com.example.a4ic1.projektkoncowyciborowski;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.preference.PreferenceGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;

import java.io.File;
import java.util.Objects;
import java.util.function.Function;

public class KolazActivity extends AppCompatActivity {

    GridView grid;
    ArrayAdapter<String> adapter;
    String[] fileNames;
    File location;

    String active;
    String newName = "";
    EditText editText;
    CheckBox checker;
    Button confirmButton;

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kolaz);
        getSupportActionBar().hide();
        init();

        editText = (EditText) findViewById(R.id.editText);
        checker = (CheckBox) findViewById(R.id.checker);
        confirmButton = (Button) findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString() != active){
                    newName = editText.getText().toString();
                    File newFile = new File(location, newName);
                    newFile.mkdir();
                    onRestart();
                }
                if( checker.isEnabled()){
                    Prefs.setMyPref(active);
                    Log.d("TARGET",active);
                    Intent intent = new Intent(KolazActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
                else{

                }
            }
        });
/*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                KolazActivity.this,     // Context
                R.layout.grid_special,     // nazwa pliku xml naszej komórki
                R.id.cell_txt,         // id pola txt w komórce
                fileNames );         // tablica przechowująca dane

        grid.setAdapter(adapter);
        */

        /*
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG","index = " + i);
            }
        });
        */
    }

    protected void init(){
        File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        location = new File(pic, "Ciborowski");
        File[] files = location.listFiles();
        fileNames = new String[files.length];

        for( int i = 0; i < files.length; i++ )
        {
            fileNames[i] = files[i].getName();
        }

        grid = (GridView) findViewById(R.id.grid);

        MyArrayAdapter<String> adapter = new MyArrayAdapter<String>(
                KolazActivity.this,     // Context
                R.layout.grid_special,     // nazwa pliku xml naszej komórki
                fileNames );         // tablica przechowująca dane

        grid.setAdapter(adapter);
    }
    public void deleteFolder(final int index){

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Uwaga!");
        alert.setMessage("Czy na pewno chcesz usunąć " + fileNames[index]);
        alert.setPositiveButton("TAK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String toDelete = fileNames[index];
                File folder = new File(location, toDelete);
                folder.delete();
                onRestart();
            }
        });
        alert.setNegativeButton("NIE", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();

    }

    public void activateFolder(int index){
        active = fileNames[index];
        editText.setText(active);
        newName = "";
    }
}
