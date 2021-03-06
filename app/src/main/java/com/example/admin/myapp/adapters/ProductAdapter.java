package com.example.admin.myapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.myapp.R;
import com.example.admin.myapp.activities.ItemDetailsActivity;
import com.example.admin.myapp.objects.Product;
import com.example.admin.myapp.utils.StoreUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.admin.myapp.utils.Constants.PRODUCT_ID;
import static com.example.admin.myapp.utils.Constants.PRODUCT_NAME;
import static com.example.admin.myapp.utils.Constants.PRODUCT_PRICE;
import static com.example.admin.myapp.utils.Constants.PRODUCT_URL;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private ArrayList<Product> productsArrayList;
    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;
    private Context context;

    public ProductAdapter(ArrayList<Product> productsArrayList, Context context) {
        this.productsArrayList = productsArrayList;
        this.context = context;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        if (position >= getItemCount() - 5 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        final Product product = productsArrayList.get(position);

        try {
            holder.productName.setText(product.getmProductName());
            String price = "Rs. " + product.getmProductPrice();
            holder.productPrice.setText(price);
            holder.productThumbnail.setImageURI(Uri.parse(product.getmImageUrl()));
            Picasso.with(context).load(product.getmImageUrl()).into(holder.productThumbnail);

            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ItemDetailsActivity.class);
                    intent.putExtra(PRODUCT_ID, product.getmProductId());
                    intent.putExtra(PRODUCT_NAME, product.getmProductName());
                    intent.putExtra(PRODUCT_URL, product.getmImageUrl());
                    intent.putExtra(PRODUCT_PRICE, product.getmProductPrice());
                    context.startActivity(intent);
                }
            });
            holder.addToWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StoreUtils imageUrlUtils = new StoreUtils();
                    imageUrlUtils.addWishlistImageUri(productsArrayList.get(position));
                    holder.addToWishlist.setImageResource(R.drawable.ic_favorite_black_18dp);
                    Toast.makeText(context, "Item added to wishlist.", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView productThumbnail, addToWishlist;
        TextView productName, productPrice;
        LinearLayout mLayoutItem;

        ProductHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mLayoutItem = itemView.findViewById(R.id.rp_layout_item);

            productThumbnail = itemView.findViewById(R.id.rp_product_image);
            productName = itemView.findViewById(R.id.rp_product_name);
            productPrice = itemView.findViewById(R.id.rp_product_price);
            addToWishlist = itemView.findViewById(R.id.rp_add_to_wishlist);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
