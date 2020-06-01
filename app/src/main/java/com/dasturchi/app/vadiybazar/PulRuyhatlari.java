package com.dasturchi.app.vadiybazar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dasturchi.app.vadiybazar.model.UserPay;
import com.dasturchi.app.vadiybazar.viewholder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PulRuyhatlari extends AppCompatActivity {

    List<UserPay> listPay;
    DatabaseReference userpay;
    TextView raqam,coin,phone,tur,navbat;
    Button btn_tulov;
    int tulovNomeri = 0;

    //RecyclerView recyclerView;
    //RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pul_ruyhatlari);

        navbat = findViewById(R.id.navbat);
        coin = findViewById(R.id.coin);
        raqam = findViewById(R.id.raqam);
        phone = findViewById(R.id.phone);
        tur = findViewById(R.id.tur);
        btn_tulov = findViewById(R.id.btn_tulov);

        List<Integer> listPayCoin = getIntent().getIntegerArrayListExtra("listPayCoin");
        List<String> listPayTur = getIntent().getStringArrayListExtra("listPayTur");
        List<String> listPayRaqam = getIntent().getStringArrayListExtra("listPayRaqam");
        final List<String> listPayPhone = getIntent().getStringArrayListExtra("listPayPhone");
        listPay = new ArrayList<>();
        for (int i=0;i<listPayCoin.size();i++){
            listPay.add(new UserPay(listPayCoin.get(i),listPayTur.get(i),
                    listPayRaqam.get(i),listPayPhone.get(i)));
        }

        userpay = FirebaseDatabase.getInstance().getReference().child("UserForPay");
        if (FirebaseDatabase.getInstance() == null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        navbat.setText("Navbat: "+tulovNomeri);
        coin.setText("coin: "+listPay.get(tulovNomeri).getCoin());
        raqam.setText("To'lov raqami: "+listPay.get(tulovNomeri).getRaqam());
        tur.setText("To'lov turi: "+listPay.get(tulovNomeri).getTur());
        phone.setText("Username: "+listPay.get(tulovNomeri).getPhone());

        btn_tulov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PulRuyhatlari.this);
                alertDialog.setTitle("To'lov o'tkazilsinmi")
                        .setMessage("To'lovni o'tkazish uchun ruxsat berasizmi!")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userpay.child(listPay.get(tulovNomeri).getPhone()).removeValue();
                                tulovNomeri++;
                                if (tulovNomeri<listPay.size()){
                                    navbat.setText("Navbat: "+tulovNomeri);
                                    coin.setText("coin: "+listPay.get(tulovNomeri).getCoin());
                                    raqam.setText("To'lov raqami: "+listPay.get(tulovNomeri).getRaqam());
                                    tur.setText("To'lov turi: "+listPay.get(tulovNomeri).getTur());
                                    phone.setText("Username: "+listPay.get(tulovNomeri).getPhone());
                                }else{
                                    navbat.setText("Boshqa mavjud emas");
                                    coin.setText("");
                                    raqam.setText("");
                                    tur.setText("");
                                    phone.setText("");
                                    startActivity(new Intent(PulRuyhatlari.this,SavolQushish.class));
                                    PulRuyhatlari.this.finish();
                                }
                            }
                        });
                alertDialog.create();
                alertDialog.show();

            }
        });

//        recyclerView = findViewById(R.id.recycler_menu);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions<UserPay> options =
//                new FirebaseRecyclerOptions.Builder<UserPay>()
//                .setQuery(userpay,UserPay.class)
//                .build();
//        FirebaseRecyclerAdapter<UserPay, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<UserPay, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final UserPay userPay) {
//                        holder.phone.setText(userPay.getPhone());
//                        holder.tur.setText(userPay.getTur());
//                        holder.raqam.setText(userPay.getRaqam());
//                        holder.coin.setText(""+userPay.getCoin());
//                        holder.btn_tulov.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PulRuyhatlari.this);
//                                alertDialog.setTitle("To'lov o'tkazilsinmi")
//                                        .setMessage("To'lovni o'tkazish uchun ruxsat berasizmi!")
//                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                userpay.child(userPay.getPhone()).removeValue();
//                                            }
//                                        });
//                                alertDialog.create();
//                                alertDialog.show();
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).
//                                inflate(R.layout.list_item_pul,parent,false);
//                        ProductViewHolder viewHolder = new
//                                ProductViewHolder(view);
//                        return viewHolder;
//                    }
//                };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//        adapter.notifyDataSetChanged();
//    }
}
