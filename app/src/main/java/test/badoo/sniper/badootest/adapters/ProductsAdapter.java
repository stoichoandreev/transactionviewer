package test.badoo.sniper.badootest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import test.badoo.sniper.badootest.R;
import test.badoo.sniper.badootest.interfaces.AdapterListener;
import test.badoo.sniper.badootest.models.Product;
import test.badoo.sniper.badootest.models.Transaction;

/**
 * Created by sniper on 1/9/16.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> implements AdapterListener{

    private Context context;
    private List<Product> products;
    private List<Transaction> transactions;
    private int lastPosition = -1;
    private AdapterListener itemClickListener;
    private ProductAdapterType adapterType;

    @Override
    public void onAdapterItemSelected(int selectedPosition) {
        itemClickListener.onAdapterItemSelected(selectedPosition);
    }

    public enum ProductAdapterType{
        TYPE_PRODUCT_ITEM,
        TYPE_TRANSACTION_ITEM;
    }
    public ProductsAdapter(Context context,AdapterListener listener,ProductAdapterType type) {
        this.products = new ArrayList<>();
        this.itemClickListener = listener;
        this.transactions = new ArrayList<>();
        this.context = context;
        this.adapterType = type;
    }

    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (adapterType){
            case TYPE_PRODUCT_ITEM:
                setupProductItem(holder,position);
                break;
            case TYPE_TRANSACTION_ITEM:
                setupTransactionItem(holder,position);
                break;
        }
        setAnimation(holder.productContainer, position);
    }

    @Override
    public int getItemCount() {
        return (adapterType == ProductAdapterType.TYPE_PRODUCT_ITEM) ? products.size() : transactions.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    public void setProducts(List<Product> newProducts, boolean needClean){
        if(needClean){
            products.clear();
        }
        products.addAll(newProducts);
        notifyDataSetChanged();
    }
    public void setTransactions(List<Transaction> newTransactions, boolean needClean){
        if(needClean){
            transactions.clear();
        }
        transactions.addAll(newTransactions);
        notifyDataSetChanged();
    }
    public String getProductName(int position){
        return products.get(position).getName();
    }
    private void setupProductItem(ViewHolder holder, int position){
        holder.headerText.setText(products.get(position).getName());
        holder.subHeaderText.setText(String.format(context.getResources().getString(R.string.transaction_number),products.get(position).getTransactionNumbers()));
    }
    private void setupTransactionItem(ViewHolder holder, int position){
        final String cantConvertedText = (transactions.get(position).hasConvertedValue()) ? "": context.getResources().getString(R.string.cant_convert);
        holder.headerText.setText(transactions.get(position).getAmount() + " " + transactions.get(position).getCurrency());
        holder.subHeaderText.setText((String.format(context.getResources().getString(R.string.pound),String.format("%.02f", transactions.get(position).getConvertedAmount()))+cantConvertedText));
    }
    public void setAdapterClickListener(AdapterListener listener){
        this.itemClickListener = listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.product_item_container) FrameLayout productContainer;
        @Bind(R.id.item_header_text) TextView headerText;
        @Bind(R.id.item_sub_header_text) TextView subHeaderText;
        private AdapterListener listener;

        public ViewHolder(View itemView,AdapterListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if(listener != null) {
                listener.onAdapterItemSelected(getAdapterPosition());
            }

        }
    }
}
