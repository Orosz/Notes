package com.orosz.myapp.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        //Shared Preferences -> For saving content on device
        sharedPreferences = this.getSharedPreferences("com.orosz.myapp.notes", Context.MODE_PRIVATE);
        //sharedPreferences.edit().putStringSet()

        set = sharedPreferences.getStringSet("notes", null);

        notes.clear();
        //convert set to array list
        if (set != null) {

            notes.addAll(set);

        } else {
            //add example note to set
            notes.add("Example Note");
            set = new HashSet<String>();
            set.addAll(notes);
            sharedPreferences.edit().putStringSet("notes", set).apply();

        }



        arrayAdapter =new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), EditYourNote.class);
                i.putExtra("noteID", position);
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this note ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                notes.remove(position);

                                savePreferences(MainActivity.this);

                                sharedPreferences.edit().remove("notes");
                                sharedPreferences.edit().putStringSet("notes", MainActivity.set).apply();
                                arrayAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.add:
                notes.add("");

                SharedPreferences sharedPreferences = this.getSharedPreferences("com.orosz.myapp.notes", Context.MODE_PRIVATE);

                if (set == null) {
                    set = new HashSet<String>();
                } else {
                    set.clear();
                }

                set.addAll(notes);

                arrayAdapter.notifyDataSetChanged();

                sharedPreferences.edit().remove("notes");
                sharedPreferences.edit().putStringSet("notes", MainActivity.set).apply();

                Intent i = new Intent(getApplicationContext(), EditYourNote.class);
                i.putExtra("noteID", notes.size()-1);
                startActivity(i);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void savePreferences(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.orosz.myapp.notes", Context.MODE_PRIVATE);

        if (set == null) {
            set = new HashSet<String>();
        } else {
            set.clear();
        }

        set.addAll(notes);
        sharedPreferences.edit().remove("notes");
        sharedPreferences.edit().putStringSet("notes", set).apply();
    }
}
