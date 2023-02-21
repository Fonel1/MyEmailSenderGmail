package com.example.senderemailaut.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.senderemailaut.Model.Receiver;
import com.example.senderemailaut.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShowEmailsBaseAdapter extends RecyclerView.Adapter<ShowEmailsBaseAdapter.MyViewHolder> {

    Context context;
    List<Receiver> receiverList;

    public ShowEmailsBaseAdapter(Context context, List<Receiver> receiverList) {
        this.context = context;
        this.receiverList = receiverList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_showemailsbase, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_date.setText(receiverList.get(position).getDate());
        holder.txt_receiverEmail.setText(receiverList.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return receiverList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        Unbinder unbinder;
        @BindView(R.id.txt_receiverEmail)
        TextView txt_receiverEmail;
        @BindView(R.id.txt_date)
        TextView txt_date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
