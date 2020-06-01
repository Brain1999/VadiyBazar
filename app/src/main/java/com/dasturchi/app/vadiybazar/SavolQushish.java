package com.dasturchi.app.vadiybazar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dasturchi.app.vadiybazar.model.Savol;
import com.dasturchi.app.vadiybazar.model.UserPay;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavolQushish extends AppCompatActivity {
    private EditText savol,variant1,variant2,variant3;
    private Button add,btn_surov;
    public List<UserPay> listPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savol_qushish);
        savol = findViewById(R.id.savol);
        variant1 = findViewById(R.id.variant1);
        variant2 = findViewById(R.id.variant2);
        variant3 = findViewById(R.id.variant3);
        add = findViewById(R.id.add);
        btn_surov = findViewById(R.id.btn_surov);

        listPay = new ArrayList<>();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s,v1,v2,v3;
                s = savol.getText().toString();
                v1 = variant1.getText().toString();
                v2 = variant2.getText().toString();
                v3 = variant3.getText().toString();
                addSavol(s,v1,v2,v3);
            }
        });
        btn_surov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavolQushish.this,PulRuyhatlari.class);
                List<String> listPayTur = new ArrayList<>();
                List<String> listPayRaqam = new ArrayList<>();
                List<String> listPayPhone = new ArrayList<>();
                List<Integer> listPayCoin = new ArrayList<>();
                for (int i=0;i<listPay.size();i++){
                    listPayCoin.add(listPay.get(i).getCoin());
                    listPayTur.add(listPay.get(i).getTur());
                    listPayRaqam.add(listPay.get(i).getRaqam());
                    listPayPhone.add(listPay.get(i).getPhone());
                }
                intent.putIntegerArrayListExtra("listPayCoin", (ArrayList<Integer>) listPayCoin);
                intent.putStringArrayListExtra("listPayTur", (ArrayList<String>) listPayTur);
                intent.putStringArrayListExtra("listPayPhone", (ArrayList<String>) listPayPhone);
                intent.putStringArrayListExtra("listPayRaqam", (ArrayList<String>) listPayRaqam);
                startActivity(intent);
            }
        });

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference().child("UserForPay");
        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserPay savol1 = dataSnapshot.getValue(UserPay.class);
                listPay.add(savol1);
                btn_surov.setText("Pul yuborish("+listPay.size()+")");
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

    private void addSavol(final String s, final String v1, final String v2, final String v3) {
        final DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference();
        if (FirebaseDatabase.getInstance() == null)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Savol sav = new Savol(s,v1,v2,v3);
                    dbRef.child("Savollar").child(s).setValue(sav).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                savol.setText("");
                                variant1.setText("");
                                variant2.setText("");
                                variant3.setText("");
                            }else{
                                Toast.makeText(SavolQushish.this, "Internetda xatolik qayta urinib ko'ring", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
