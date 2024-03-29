package com.example.zpi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zpi.R;
import com.example.zpi.data_handling.SharedPreferencesHandler;
import com.example.zpi.models.Debt;
import com.example.zpi.models.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.ViewHolder> {

    List<Debt> debts;
    private ClickListener clickListener;
    User loggedUser;

    public FinanceAdapter(List<Debt> debts, User loggedUser) {
        this.debts = debts;
        this.loggedUser = loggedUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_finance, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Debt debt = debts.get(position);

        TextView fromTV = holder.fromTV;
        TextView toTV = holder.toTV;
        TextView amountTv = holder.amountTV;
        TextView initialsTV = holder.initialsTV;

        /*if(debt.getTo().getID() == loggedUser.getID()){
            CardView card = holder.card;
            card.getBackground().setTint(Color.parseColor("#96D76F"));
        }
        if(debt.getFrom().getID() == loggedUser.getID()){
            CardView card = holder.card;
            card.getBackground().setTint(Color.parseColor("#A30000"));
        }*/

        fromTV.setText(debt.getFrom().getName() + " " + debt.getFrom().getSurname());
        toTV.setText(debt.getTo().getName() + " " + debt.getTo().getSurname());
        amountTv.setText(String.valueOf(debt.getAmount().intValue()) + " zł");
        initialsTV.setText(String.valueOf(debt.getFrom().getName().charAt(0)).toUpperCase() + String.valueOf(debt.getFrom().getSurname().charAt(0)).toUpperCase());
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return debts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView fromTV;
        public TextView toTV;
        public TextView amountTV;
        public TextView initialsTV;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            fromTV = (TextView) itemView.findViewById(R.id.tv_from);
            toTV = (TextView) itemView.findViewById(R.id.tv_to);
            amountTV = (TextView) itemView.findViewById(R.id.tv_amount);
            initialsTV = (TextView) itemView.findViewById(R.id.tv_initials);
            card = (CardView) itemView.findViewById(R.id.cv_initials);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }

    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


}
