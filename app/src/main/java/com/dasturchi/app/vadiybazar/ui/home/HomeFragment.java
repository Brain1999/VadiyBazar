package com.dasturchi.app.vadiybazar.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.dasturchi.app.vadiybazar.HomeActivity;
import com.dasturchi.app.vadiybazar.R;
import com.dasturchi.app.vadiybazar.model.Savol;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment{

    private ArrayList<Savol> arrayList;
    private Button btn_javob,btn_next;
    private RadioGroup radioGroup;
    private RadioButton rb1,rb2,rb3;
    private TextView savol;
    public static TextView coin;
    private int savolNomeri = 0;
    private boolean rew=true,rew2=true;
    public static String phone;
    private RewardedAd rewardedAd;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    public static RewardedVideoAd mRewardedVideoAd;

    String javob = "";
    DbHelper db;
    int iii=0;
    int kl=0;

    private ProgressDialog loadingBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        savol = root.findViewById(R.id.savol);
        radioGroup = root.findViewById(R.id.radiogroup);
        rb1 = root.findViewById(R.id.radiobtn1);
        rb2 = root.findViewById(R.id.radiobtn2);
        rb3 = root.findViewById(R.id.radiobtn3);
        coin = root.findViewById(R.id.coin);
        btn_javob = root.findViewById(R.id.btn_javob);
        btn_next = root.findViewById(R.id.btn_next);
        arrayList = new ArrayList<>();

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(getContext(), "ca-aapp-pub-5730565650908468~1565313328");
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener((RewardedVideoAdListener) getContext());

        rewardedAd = new RewardedAd(getContext(),
                "ca-app-pub-5730565650908468/2989327203");


        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-5730565650908468/6174179150");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                DatabaseReference rootref;
                rootref = FirebaseDatabase.getInstance().getReference("Users").child(phone);
                int c = Integer.parseInt(coin.getText().toString());
                c = c+5;
                final int finalC = c;
                rootref.child("coin").setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            coin.setText(finalC +"");
                        }else{
                            Toast.makeText(getContext(), "Internetda mavjud emas!!!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), "Coin yig'ilishi uchun internetga ulaning!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        DbHelper db0 = new DbHelper(getContext());
        ArrayList<String> list = new ArrayList<>();
        list.addAll(db0.getLogin());
        if (list == null){
        }else if (list.size()>1){
            phone = list.get(0);
        }

        DatabaseReference rootrefTh;
        rootrefTh = FirebaseDatabase.getInstance().getReference("Users").child(phone).child("coin");
        if (FirebaseDatabase.getInstance() == null)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        rootrefTh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int coin1 = dataSnapshot.getValue(Integer.class);
                coin.setText(coin1+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        db = new DbHelper(getContext());
        final Cursor cursor = db.getSavolNomeri();
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                String s = cursor.getString(1);
                int i = Integer.parseInt(s);
                savolNomeri = i;
            }
        }else {
            savolNomeri = 0;
            db.setSavolNomeri();
        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference().child("Savollar");
        if (FirebaseDatabase.getInstance() == null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Savol savol1 = dataSnapshot.getValue(Savol.class);
                arrayList.add(savol1);
                iii++;
                if (arrayList.size()>savolNomeri){
                    String v1 = arrayList.get(savolNomeri).getV1();
                    String v2 = arrayList.get(savolNomeri).getV2();
                    String v3 = arrayList.get(savolNomeri).getV3();
                    if(v1.length()>4){
                        if (v1.substring(v1.length()-5).equals("trues")){
                            rb1.setText(v1.substring(0,v1.length()-5));
                        }else{
                            rb1.setText(v1);
                        }
                    }else{
                        rb1.setText(v1);
                    }
                    if(v2.length()>4){
                        if (v2.substring(v2.length()-5).equals("trues")){
                            rb2.setText(v2.substring(0,v2.length()-5));
                        }else{
                            rb2.setText(v2);
                        }
                    }else{
                        rb2.setText(v2);
                    }
                    if(v3.length()>4){
                        if (v3.substring(v3.length()-5).equals("trues")){
                            rb3.setText(v3.substring(0,v3.length()-5));
                        }else{
                            rb3.setText(v3);
                        }
                    }else{
                        rb3.setText(v3);
                    }
                    savol.setText(arrayList.get(savolNomeri).getS());
                }
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
                Toast.makeText(getContext(), ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
        btn_javob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savolNomeri>iii-1){
                    db.updateSavolNomeri("0");
                }
                if (rb1.isChecked()||rb2.isChecked()||rb3.isChecked()){
                    int id = radioGroup.getCheckedRadioButtonId();
                    RadioButton rbJ = root.findViewById(id);
                    javob = rbJ.getText().toString();
                    String v1 = arrayList.get(savolNomeri).getV1();
                    String v2 = arrayList.get(savolNomeri).getV2();
                    String v3 = arrayList.get(savolNomeri).getV3();
                    if (v1.length()>4){
                        v1 = v1.substring(0,v1.length()-5);
                    }else{
                        v1 = "";
                    }
                    if(v2.length()>4){
                        v2 = v2.substring(0,v2.length()-5);
                    }else{
                        v2="";
                    }
                    if (v3.length()>4){
                        v3 = v3.substring(0,v3.length()-5);
                    }else{
                        v3="";
                    }
                    if (javob.equals(v1)||javob.equals(v2)||javob.equals(v3)){
                        DatabaseReference rootref;
                        rootref = FirebaseDatabase.getInstance().getReference("Users").child(phone);
                        int c = Integer.parseInt(coin.getText().toString());
                        c = c+1;
                        final int finalC = c;
                        rootref.child("coin").setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    coin.setText(finalC +"");
                                }else{
                                    Toast.makeText(getContext(), "Internetda mavjud emas!!!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(), "Coin yig'ilishi uchun internetga ulaning!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    btn_next.setVisibility(View.VISIBLE);
                    btn_javob.setVisibility(View.INVISIBLE);
                    if (arrayList.size()>savolNomeri+1){
                        db.updateSavolNomeri(savolNomeri+1+"");
                    }else{
                        db.updateSavolNomeri("0");
                    }
                }else{
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_javob.setVisibility( View.VISIBLE);
                btn_next.setVisibility(View.INVISIBLE);
                Cursor cursor1 = db.getSavolNomeri();
                if (cursor1.getCount()>0){
                    while (cursor1.moveToNext()){
                        String sv = cursor1.getString(1);
                        savolNomeri = Integer.parseInt(sv);
                        break;
                    }
                }
                radioGroup.clearCheck();
                String v1 = arrayList.get(savolNomeri).getV1();
                String v2 = arrayList.get(savolNomeri).getV2();
                String v3 = arrayList.get(savolNomeri).getV3();
                if(v1.length()>4){
                    if (v1.substring(v1.length()-5).equals("trues")){
                        rb1.setText(v1.substring(0,v1.length()-5));
                    }else{
                        rb1.setText(v1);
                    }
                }else{
                    rb1.setText(v1);
                }
                if(v2.length()>4){
                    if (v2.substring(v2.length()-5).equals("trues")){
                        rb2.setText(v2.substring(0,v2.length()-5));
                    }else{
                        rb2.setText(v2);
                    }
                }else{
                    rb2.setText(v2);
                }
                if(v3.length()>4){
                    if (v3.substring(v3.length()-5).equals("trues")){
                        rb3.setText(v3.substring(0,v3.length()-5));
                    }else{
                        rb3.setText(v3);
                    }
                }else{
                    rb3.setText(v3);
                }
                kl++;
                savol.setText(arrayList.get(savolNomeri).getS());
                if (kl == 10){
                    RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback(){
                        @Override
                        public void onRewardedAdLoaded() {
                            Activity activity = getActivity();
                            RewardedAdCallback rewardedAdCallback = new RewardedAdCallback() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    DatabaseReference rootref;
                                    rootref = FirebaseDatabase.getInstance().getReference("Users").child(phone);
                                    int c = Integer.parseInt(coin.getText().toString());
                                    c = c+150;
                                    final int finalC = c;
                                    rootref.child("coin").setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                coin.setText(finalC +"");
                                            }else{
                                                Toast.makeText(getContext(), "Internetda mavjud emas!!!", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(getContext(), "Coin yig'ilishi uchun internetga ulaning!!!", Toast.LENGTH_SHORT).show();
                                            }
                                                                    }
                                    });
                                }

                                @Override
                                public void onRewardedAdOpened() {
                                    super.onRewardedAdOpened();
                                }

                                @Override
                                public void onRewardedAdClosed() {
                                    super.onRewardedAdClosed();
                                }

                                @Override
                                public void onRewardedAdFailedToShow(int i) {
                                    super.onRewardedAdFailedToShow(i);
                                }
                            };
                            rewardedAd.show(activity,rewardedAdCallback);
                            super.onRewardedAdLoaded();
                        }

                        @Override
                        public void onRewardedAdFailedToLoad(int i) {
                            super.onRewardedAdFailedToLoad(i);
                        }
                    };
                    rewardedAd.loadAd(new AdRequest.Builder().build(),adLoadCallback);

                }
                if(kl == 5){
                    if (mRewardedVideoAd.isLoaded()){
                        mRewardedVideoAd.show();
                    }
                }
            }
        });
        loadRewardedVideoAd();
        return root;
    }
    public static void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-5730565650908468/9702360627",
                new AdRequest.Builder().build());
    }

}
// app id -->    ca-app-pub-5730565650908468~1565313328
// banner id --> ca-app-pub-5730565650908468/9691215356
// inter id --> ca-app-pub-5730565650908468/6174179150
// reward1 id --> ca-app-pub-5730565650908468/2989327203
// rewardvideo id --> ca-app-pub-5730565650908468/9702360627