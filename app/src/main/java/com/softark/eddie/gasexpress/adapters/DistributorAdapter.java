package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.activities.PriceActivity;

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.ViewHolder> {

    private final String[] names;
    private final Context context;
    private final LayoutInflater inflater;

    public DistributorAdapter(String[] names, Context context) {
        this.names = names;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    @Override
    public void onBindViewHolder(DistributorAdapter.ViewHolder holder, int position) {
        String name = names[position];
        holder.gasSize.setText(name);

            if (names[position].toLowerCase().equals("flour")) {
                holder.image.setImageResource(R.drawable.flour);
            } else if (names[position].toLowerCase().equals("rice")) {
                holder.image.setImageResource(R.drawable.rice);
            } else if (names[position].toLowerCase().equals("cooking oil")) {
                holder.image.setImageResource(R.drawable.oil1);
            } else if (names[position].toLowerCase().equals("sugar")) {
                holder.image.setImageResource(R.drawable.sugar1);
            } else if (names[position].toLowerCase().equals("salt")) {
                holder.image.setImageResource(R.drawable.salt);
            } else if (names[position].toLowerCase().equals("soap")) {
                holder.image.setImageResource(R.drawable.soap);
            } else if (names[position].toLowerCase().equals("tooth paste")) {
                holder.image.setImageResource(R.drawable.toothpaste);
            } else if (names[position].toLowerCase().equals("shoe polish")) {
                holder.image.setImageResource(R.drawable.kiwi);
            } else if (names[position].toLowerCase().equals("bread")) {
                holder.image.setImageResource(R.drawable.bread);
            } else if (names[position].toLowerCase().equals("detergents")) {
                holder.image.setImageResource(R.drawable.detergent);
            } else if (names[position].toLowerCase().equals("beauty products")) {
                holder.image.setImageResource(R.drawable.beauty);
            }
    }

    @Override
    public DistributorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.distributor_list, null);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView gasSize;
        public final ImageView image;
        public final View view;
//        public final ImageButton moreInfo;
        public final View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            image = (ImageView) itemView.findViewById(R.id.size_image);
            gasSize = (TextView) itemView.findViewById(R.id.gas_size);

            mainView = itemView;
            mainView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String name = names[getAdapterPosition()];
            Intent intent = new Intent(context, PriceActivity.class);
            Bundle b = new Bundle();
            b.putString("name", name);
            intent.putExtras(b);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

}
