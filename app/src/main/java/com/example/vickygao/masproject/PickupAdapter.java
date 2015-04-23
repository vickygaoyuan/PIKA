package com.example.vickygao.masproject;

/**
 * Created by ChesterGuo on 2/21/15.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import java.util.List;
import android.content.Context;

import android.content.Intent;
import android.view.View.OnClickListener;

import org.json.JSONObject;

public class PickupAdapter extends RecyclerView.Adapter<PickupAdapter.ContactViewHolder> {
    private List<infocard> cardList;
    private Context mContext;

    public PickupAdapter(Context context,List<infocard> contactList) {
        this.mContext = context;
        this.cardList = contactList;
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, final int i) {
        infocard ci = cardList.get(i);
        contactViewHolder.vName.setText(ci.getUsername());
        contactViewHolder.vSurname.setText(ci.getGiver());
        //contactViewHolder.vEmail.setText(ci.email);
        switch (ci.getStatus()){
        case "PAYMENT-PENDING":
            contactViewHolder.vImage.setImageResource(R.drawable.a1);

            break;
        case "PAYED":
                contactViewHolder.vImage.setImageResource(R.drawable.a1);

                break;
            case "MATCHED":
                contactViewHolder.vImage.setImageResource(R.drawable.a2);

                break;
            case "SHIPPING":
                contactViewHolder.vImage.setImageResource(R.drawable.a3);

                break;
            case "DELIVERED":
                contactViewHolder.vImage.setImageResource(R.drawable.a4);

                break;}

        contactViewHolder.vStatus.setText(ci.getStatus());
        contactViewHolder.vTitle.setText(ci.getName());
        contactViewHolder.vButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();


                intent.putExtra("username", cardList.get(i).getUsername());
                intent.putExtra("driver", cardList.get(i).getDriver());
                intent.putExtra("givername", cardList.get(i).getGiver());
                intent.putExtra("deliveryaddress", cardList.get(i).getDeliverAddress());
                intent.putExtra("status", cardList.get(i).getStatus());
                intent.putExtra("item", cardList.get(i).getName());
                intent.putExtra("pickupaddress", cardList.get(i).getPickupAddress());
                intent.putExtra("fee", cardList.get(i).getFee());
                intent.putExtra("givernum", cardList.get(i).getGiverNum());
                intent.putExtra("pickupTime", cardList.get(i).getRequestTime());

                intent.setClass(mContext,DetailCheckActivity.class);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vSurname;
        //protected TextView vEmail;
        protected TextView vTitle;
        protected ImageView vImage;
        protected TextView vStatus;
        protected Button vButton;

        public ContactViewHolder(View v) {
            super(v);
            vImage =(ImageView)v.findViewById(R.id.imageView2);
            vName = (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView) v.findViewById(R.id.txtSurname);
            // vEmail = (TextView) v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
            vStatus = (TextView) v.findViewById(R.id.txtStatus);
            vButton = (Button) v.findViewById(R.id.confirm);
        }
    }
}

