package com.dasturchi.app.vadiybazar.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dasturchi.app.vadiybazar.R;
import com.dasturchi.app.vadiybazar.interfase.ItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView phone,tur,raqam,coin;
    public Button btn_tulov;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        phone = itemView.findViewById(R.id.phone);
        tur = itemView.findViewById(R.id.tur);
        raqam = itemView.findViewById(R.id.raqam);
        coin = itemView.findViewById(R.id.coin);
        btn_tulov= itemView.findViewById(R.id.btn_tulov);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
