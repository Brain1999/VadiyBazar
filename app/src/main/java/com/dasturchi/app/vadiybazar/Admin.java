package com.dasturchi.app.vadiybazar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dasturchi.app.vadiybazar.model.Admins;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin extends AppCompatActivity {
    EditText login,parol;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        login = findViewById(R.id.login);
        parol = findViewById(R.id.parol);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String log = login.getText().toString();
                String par = parol.getText().toString();
                allowAccesToAccount(par,log);
            }
        });
    }
    private void allowAccesToAccount(final String parol, final String login) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        if (FirebaseDatabase.getInstance() == null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Admins").child(login).exists()){
                    Admins user = dataSnapshot.child("Admins").child(login).getValue(Admins.class);
                    if (user.getLogin().equals(login)){
                        if (user.getParol().equals(parol)){
                            Toast.makeText(Admin.this, "Muvaffaqiyatli tekshirildi!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Admin.this, SavolQushish.class));
                        }else{
                            Toast.makeText(Admin.this, "parol xato", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(Admin.this, "mavjud emas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
