package com.dasturchi.app.vadiybazar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dasturchi.app.vadiybazar.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button main_join,main_login;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String phone="";
        String parol="";
        DbHelper db = new DbHelper(this);
        ArrayList<String> list = new ArrayList<>();
        list.addAll(db.getLogin());
        if (list == null){
        }else if (list.size()>1){
            phone = list.get(0);
            parol = list.get(1);
        }

        if (!phone.equals("") && !parol.equals("")){
            loadingBar = new ProgressDialog(this);
            loadingBar.setTitle("Accountga kirish!");
            loadingBar.setMessage("Iltimos kuting tekshirilmoqda...");
            loadingBar.setCanceledOnTouchOutside(false);
            allowAccesToAccount(parol,phone);
            loadingBar.show();
        }

        biriktirishId();
        main_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        main_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });




//        TextView text = findViewById(R.id.text);
//        text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDatabase = FirebaseDatabase.getInstance().getReference();
//                User user = new User("nam","12","123");
//                mDatabase.child("Users").child(user.getPhone()).setValue(user);
//            }
//        });
    }
    private void biriktirishId() {
        main_join = findViewById(R.id.main_join_now_btn);
        main_login = findViewById(R.id.main_login_btn);
    }
    private void allowAccesToAccount(final String parol, final String phone) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        if (FirebaseDatabase.getInstance() == null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()){
                    User user = dataSnapshot.child("Users").child(phone).getValue(User.class);
                    if (user.getPhone().equals(phone)){
                        if (user.getParol().equals(parol)){
                            Toast.makeText(MainActivity.this, "Muvaffaqiyatli tekshirildi!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            MainActivity.this.finish();
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }else{
                            loadingBar.dismiss();
                        }
                    }
                }else{
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
