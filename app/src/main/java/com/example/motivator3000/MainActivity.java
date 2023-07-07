package com.example.motivator3000;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText edName;
    private DatabaseReference mDataBase;
    private String USER_KEY = "Users";
    private Button btnEnter;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Initialize Firebase DataBase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDataBase = database.getReference();
        //initialization
        edName = findViewById(R.id.edName);
        btnEnter = findViewById(R.id.btnEnter);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edName.getText().toString().matches("")){
                    Toast.makeText(v.getContext(),"Для продолжения введите имя!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(currentUser == null)
                {
                    mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                //зарегистрирован как новый пользователь currentUser.getUid()
                                String id = currentUser.getUid();
                                String name = edName.getText().toString();
                                int count = 0;

                                User newUser = new User(id, name, count);

                                if(!TextUtils.isEmpty(name))
                                {
                                    mDataBase.push().setValue(newUser);
                                }
                                else
                                {
                                    //Toast.makeText(v.getContext(),"Для продолжения введите имя!", Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG",e.getMessage());
                        }
                    });
                }
                else
                {
                    //Уже зарегистрирован currentUser.getUid()
                    String userId = currentUser.getUid();
                    String name = edName.getText().toString();

                    /*
                    int count = 0;

                    User newUser = new User(userId, name, count);


                    if(!TextUtils.isEmpty(name))
                    {
                        mDataBase.child("users").child(userId).child("name").setValue(name);
                        mDataBase.child("users").child(userId).child("count").setValue(count);
                        //mDataBase.push().setValue(newUser);
                    }
                    else
                    {
                        //Toast.makeText(this,"Пустое поле", Toast.LENGTH_SHORT);
                    }
                    */
                    Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            }
        });
    }

    public void init()
    {
        //edName = findViewById(R.id.edName);
        //mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

    }
    public void onClickEnter(View view)
    {
//        String id = mDataBase.getKey();
//        String name = edName.getText().toString();
//        String count = "0";
//
//        User newUser = new User(id, name, count);
//
//        if(!TextUtils.isEmpty(name))
//        {
//            mDataBase.push().setValue(newUser);
//        }
//        else
//        {
//            Toast.makeText(this,"Пустое поле", Toast.LENGTH_SHORT);
//        }
//
//        Intent intent = new Intent(MainActivity.this, ReadActivity.class);
//        //intent.putExtra("name", name);
//        startActivity(intent);
    }
}