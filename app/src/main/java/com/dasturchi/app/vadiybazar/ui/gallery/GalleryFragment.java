package com.dasturchi.app.vadiybazar.ui.gallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dasturchi.app.vadiybazar.DbHelper;
import com.dasturchi.app.vadiybazar.R;
import com.dasturchi.app.vadiybazar.model.UserPay;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.lang.System.exit;

public class GalleryFragment extends Fragment {
    RadioGroup radioGroup;
    RadioButton radioButtoTel,radioButtonKarta;
    EditText editTel,editKarta,editParol;
    Button btnYechish;
    String phone,parol;
    int coin;
    private ProgressDialog loadingBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        radioGroup = root.findViewById(R.id.radiogroup);
        radioButtoTel = root.findViewById(R.id.radio_paynet);
        radioButtonKarta = root.findViewById(R.id.radio_plastik);
        editKarta = root.findViewById(R.id.editKarta);
        editTel = root.findViewById(R.id.editTel);
        editParol = root.findViewById(R.id.editPassword);
        btnYechish = root.findViewById(R.id.btnYechish);

        loadingBar = new ProgressDialog(getContext());
        loadingBar.setTitle("Accountga kirish!");
        loadingBar.setMessage("Iltimos kuting tekshirilmoqda...");
        loadingBar.setCanceledOnTouchOutside(false);

        DbHelper db = new DbHelper(getContext());
        ArrayList<String> list = new ArrayList<>();
        list.addAll(db.getLogin());
        if (list == null){
        }else if (list.size()>1){
            phone = list.get(0);
            parol = list.get(1);
        }

        DatabaseReference rootrefTh;
        rootrefTh = FirebaseDatabase.getInstance().getReference("Users").child(phone).child("coin");
        if (FirebaseDatabase.getInstance() == null)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        rootrefTh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                coin = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_paynet){
                    editKarta.setVisibility(View.INVISIBLE);
                    editTel.setVisibility(View.VISIBLE);
                }
                if (checkedId == R.id.radio_plastik){
                    editKarta.setVisibility(View.VISIBLE);
                    editTel.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnYechish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coin>50000) {

                    String karta = editKarta.getText().toString();
                    String tel = editTel.getText().toString();
                    String pass = editParol.getText().toString();
                    int id = radioGroup.getCheckedRadioButtonId();
                    if (id == R.id.radio_plastik) {
                        if (TextUtils.isEmpty(karta)) {
                            Toast.makeText(getContext(), "Karta raqam kiritilmagan!", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(pass)) {
                            Toast.makeText(getContext(), "Parol kiritilmagan", Toast.LENGTH_SHORT).show();
                        } else {
                            if (pass.equals(parol)) {
                                loadingBar.show();
                                DatabaseReference reference;
                                reference = FirebaseDatabase.getInstance().getReference();
                                if (FirebaseDatabase.getInstance() == null) {
                                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                                }
                                UserPay userPay = new UserPay(coin,"Karta",karta,phone);
                                reference.child("UserForPay").child(phone).setValue(userPay).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            DatabaseReference reference;
                                            reference = FirebaseDatabase.getInstance().getReference();
                                            if (FirebaseDatabase.getInstance() == null) {
                                                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                                            }
                                            reference.child("Users").child(phone).child("coin").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        loadingBar.dismiss();
                                                        coin=0;
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setMessage(("So'rov jo'natildi! Pulingiz 24 soat ichida o'tkaziladi!"))
                                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                    }
                                                                });
                                                        builder.create();
                                                        builder.show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                    if (id == R.id.radio_paynet) {
                        if (TextUtils.isEmpty(tel)) {
                            Toast.makeText(getContext(), "Telefon raqam kiritilmagan!", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(pass)) {
                            Toast.makeText(getContext(), "Parol kiritilmagan", Toast.LENGTH_SHORT).show();
                        } else {
                            if (pass.equals(parol)) {
                                loadingBar.show();
                                DatabaseReference reference;
                                reference = FirebaseDatabase.getInstance().getReference();
                                if (FirebaseDatabase.getInstance() == null) {
                                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                                }
                                UserPay userPay = new UserPay(coin,"Paynet",tel,phone);
                                reference.child("UserForPay").child(phone).setValue(userPay).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            DatabaseReference reference;
                                            reference = FirebaseDatabase.getInstance().getReference();
                                            if (FirebaseDatabase.getInstance() == null) {
                                                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                                            }
                                            reference.child("Users").child(phone).child("coin").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        loadingBar.dismiss();
                                                        coin=0;
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setMessage(("So'rov jo'natildi! Pulingiz 24 soat ichida o'tkaziladi!"))
                                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                    }
                                                                });
                                                        builder.create();
                                                        builder.show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "Sizning tangalaringiz 50000 dan oshmagan!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "50000 tangadan yuqorisini yechib olish mumkin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}