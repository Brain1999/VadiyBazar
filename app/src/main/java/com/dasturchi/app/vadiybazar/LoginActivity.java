package com.dasturchi.app.vadiybazar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.dasturchi.app.vadiybazar.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText phone,parol;
    private Button loginBtn;
    private ProgressDialog loadingBar;
    private CheckBox rememberMe;
    private ArrayList<User> userArrayList;
    int j=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone = findViewById(R.id.login_phone_number_input);
        parol = findViewById(R.id.login_password_input);
        loginBtn = findViewById(R.id.login_btn);
        rememberMe = findViewById(R.id.remember_me_checkbox);
        loadingBar = new ProgressDialog(this);
        userArrayList = new ArrayList<>();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loginUser();
            }
        });

    }

    private void loginUser() {
        String phone1 = phone.getText().toString();
        String parol1 = parol.getText().toString();
        if (TextUtils.isEmpty(phone1)){
            Toast.makeText(this, "Telefon raqam kiriting!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(parol1)){
            Toast.makeText(this, "Parolni kiriting", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Accountga kirish!");
            loadingBar.setMessage("Iltimos kuting tekshirilmoqda...");
            loadingBar.setCanceledOnTouchOutside(false);
            allowAccesToAccount(parol1,phone1);
            loadingBar.show();
        }
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
                            if (rememberMe.isChecked()){
                                DbHelper db = new DbHelper(LoginActivity.this);
                                db.setLogin(phone,parol);
                            }
                            Toast.makeText(LoginActivity.this, "Muvaffaqiyatli tekshirildi!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            LoginActivity.this.finish();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "Parol noto'g'ri", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Bu "+phone+" raqam uchun account mavjud emas!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Siz yangi account yaratishingiz mumkin!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference root;
        root = FirebaseDatabase.getInstance().getReference().child("Users");
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user =  dataSnapshot.getValue(User.class);
                userArrayList.add(user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
