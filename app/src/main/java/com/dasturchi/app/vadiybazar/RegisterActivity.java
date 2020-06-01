package com.dasturchi.app.vadiybazar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dasturchi.app.vadiybazar.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccountButton;
    private EditText inputName,inpunPhone,inputParol;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createAccountButton = findViewById(R.id.register_btn);
        inputName = findViewById(R.id.register_name_input);
        inpunPhone = findViewById(R.id.register_phone_number_input);
        inputParol = findViewById(R.id.register_parol_input);
        loadingBar = new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void createAccount() {
        String name = inputName.getText().toString();
        String phone = inpunPhone.getText().toString();
        String parol = inputParol.getText().toString();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Ismni kiriting", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Telefon raqamni kiriting", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(parol)){
            Toast.makeText(this, "Parol tanlang", Toast.LENGTH_SHORT).show();
        }
        else{
            User user = new User(name,phone,parol,0);
            loadingBar.setTitle("Account yaratish!");
            loadingBar.setMessage("Iltimos kuting tekshirilmoqda...");
            loadingBar.setCanceledOnTouchOutside(false);
            validatePhoneNumber(user);
            loadingBar.show();
        }
    }

    private void validatePhoneNumber(final User user) {
        final DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference();
        if (FirebaseDatabase.getInstance() == null)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("Users").child(user.getPhone()).exists()){
                    dbRef.child("Users").child(user.getPhone()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Tabriklaymiz siz ro'yhatdan o'tdingiz!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                RegisterActivity.this.finish();
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            }else{
                                Toast.makeText(RegisterActivity.this, "Internetda xatolik qayta urinib ko'ring", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this, "Bu raqam ro'yhatdan o'tgan", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, "Boshqa raqam bilan urinib ko'ring", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    RegisterActivity.this.finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
//
//
    }
}
