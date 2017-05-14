package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.models.Accessory;

import java.util.ArrayList;

public class CartAccessoryAdapter extends RecyclerView.Adapter<CartAccessoryAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Accessory> items;
    private final LayoutInflater inflater;
    private final TextView totalPrice;

    public CartAccessoryAdapter(Context context, ArrayList<Accessory> accessories, TextView totalPrice) {
        this.context = context;
        this.items = accessories;
        inflater = LayoutInflater.from(context);
        this.totalPrice = totalPrice;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public CartAccessoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list, null);
        return new CartAccessoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartAccessoryAdapter.ViewHolder holder, int position) {
        Accessory item = items.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(String.valueOf(item.getPrice()));
        holder.quantitySelect.setValue(item.getQuantity());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, NumberPicker.OnScrollListener, NumberPicker.OnValueChangeListener {
        public final TextView name;
        public final TextView price;
        public final ImageButton remove;
        public final NumberPicker quantitySelect;
        private int scrollState = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cart_item_name);
            price = (TextView) itemView.findViewById(R.id.cart_item_price);
            remove = (ImageButton) itemView.findViewById(R.id.remove_from_cart);
            remove.setOnClickListener(this);
            quantitySelect = (NumberPicker) itemView.findViewById(R.id.quantity_select);
            quantitySelect.setMinValue(1);
            quantitySelect.setMaxValue(100);
            quantitySelect.setWrapSelectorWheel(false);
            quantitySelect.setOnValueChangedListener(this);
            quantitySelect.setOnScrollListener(this);
        }

        @Override
        public void onClick(View v) {
            Accessory accessory = items.get(getAdapterPosition());
            Cart.removeProduct(accessory);
            totalPrice.setText(String.valueOf(Cart.totalPrice));
            items.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), items.size());
            notifyDataSetChanged();
        }

        @Override
        public void onScrollStateChange(NumberPicker view, int scrollState) {
            this.scrollState = scrollState;
            if(scrollState == SCROLL_STATE_IDLE) {
                Accessory accessory = items.get(getAdapterPosition());
                Cart.updateCartItem(accessory, Cart.ACCESSORIES, view.getValue());
                accessory.setQuantity(view.getValue());
                totalPrice.setText(String.valueOf(Cart.totalPrice));
            }
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if(scrollState == SCROLL_STATE_IDLE) {
                Accessory accessory = items.get(getAdapterPosition());
                Cart.updateCartItem(accessory, Cart.ACCESSORIES, picker.getValue());
                accessory.setQuantity(picker.getValue());
                totalPrice.setText(String.valueOf(Cart.totalPrice));
            }
        }
    }
}
