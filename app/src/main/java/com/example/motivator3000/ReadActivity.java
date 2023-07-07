package com.example.motivator3000;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.os.Vibrator;
public class ReadActivity extends AppCompatActivity{
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private DatabaseReference mDataBase;
    private String USER_KEY = "users";
    public String userId;
    private int count;
    private Toolbar mToolbar;
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_layout);
        init();
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void init()
    {

        listView = findViewById(R.id.listView);
        listData = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        getDataFromDB();

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null)
        {
            userId = arguments.get("userId").toString();
        }
    }

    private void getDataFromDB()
    {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size() > 0) listData.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    User user = ds.getValue(User.class);
                    assert user != null;
                    listData.add("Имя: " +user.name +"    Счёт: "+ user.count);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBase.addValueEventListener(vListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        load_Param_from_Firebase();
    }

    private void load_Param_from_Firebase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("users/"+ userId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("count").getValue() != null) {
                    count = snapshot.child("count").getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                increaseCount();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                increaseCount();
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void increaseCount(){
        count++;

        if(count<=18)
        {
            mDataBase.child(userId).child("count").setValue(count);
        }

        Vibrator();

        if(count == 18)
        {
            Toast.makeText(this, "ПОБЕДА!", Toast.LENGTH_SHORT).show();
            mPlayer= MediaPlayer.create(this, R.raw.tiger);
            mPlayer.start();

        }
    }
    public void onClickCount(View view){
        increaseCount();
    }

    public void Vibrator(){
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            //deprecated in API 26
            v.vibrate(200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*
        int id = item.getItemId();
        switch (id){
            case R.id.newGame:
                Toast.makeText(this, "New game", Toast.LENGTH_SHORT).show();
                break;
        }
         */
        return true;
    }
}
