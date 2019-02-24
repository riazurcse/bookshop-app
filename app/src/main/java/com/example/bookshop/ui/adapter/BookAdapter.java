package com.example.bookshop.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookshop.R;
import com.example.bookshop.common.Constants;
import com.example.bookshop.model.Book;
import com.example.bookshop.utils.PreferenceHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private static final String TAG = BookAdapter.class.getSimpleName();

    int NO_OPERATION = 0;
    int CART = 1;
    int WISHLIST = 2;

    private List<Book> books;
    private List<Book> cartBooks;
    private List<Book> wishlistBooks;
    private int rowLayout;
    private Context mContext;
    private PreferenceHelper preferenceHelper;
    private Gson gson;

    public BookAdapter(Context context, int rowLayout, List<Book> books) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.books = books;
        this.preferenceHelper = new PreferenceHelper(context);
        this.gson = new Gson();
        getSavedData();
    }

    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new BookAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder holder, final int position) {

        int operationType = contains(books.get(position));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.bookTitleTV.getLayoutParams();
        if (operationType == NO_OPERATION) {
            params.setMargins(0, 0,0,0);
            holder.cartWishlistIconHolder.setVisibility(View.GONE);
            holder.bookTitleTV.setLayoutParams(params);
        }
        else if (operationType == CART) {
            params.setMargins(0, -30,0,0);
            holder.cartWishlistIconHolder.setVisibility(View.VISIBLE);
            holder.bookTitleTV.setLayoutParams(params);
            holder.cartWishlistIV.setImageResource(R.drawable.ic_cart);
            holder.cartWishlistIV.setColorFilter(mContext.getResources().getColor(R.color.addToCartButtonColor), PorterDuff.Mode.SRC_IN);
        }
        else if (operationType == WISHLIST) {
            params.setMargins(0, -30,0,0);
            holder.cartWishlistIconHolder.setVisibility(View.VISIBLE);
            holder.bookTitleTV.setLayoutParams(params);
            holder.cartWishlistIV.setImageResource(R.drawable.ic_wishlist);
            holder.cartWishlistIV.setColorFilter(mContext.getResources().getColor(R.color.wishlistButtonColor), PorterDuff.Mode.SRC_IN);
        }

        holder.bookTitleTV.setText(books.get(position).getTitle() != null ? books.get(position).getTitle() : "N/A");
        holder.bookSubtitleTV.setText(books.get(position).getSubTitle() != null ? books.get(position).getSubTitle() : "N/A");

        String previewURL = books.get(position).getPreview() != null ? books.get(position).getPreview() : "";
        if (previewURL.length() > 0) {
            Glide.with(mContext).load(previewURL).placeholder(R.drawable.ic_placeholder).into(holder.bookIV);
        }
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView bookTitleTV, bookSubtitleTV;
        public ImageView bookIV, cartWishlistIV;
        public LinearLayout cartWishlistIconHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitleTV = (TextView) itemView.findViewById(R.id.bookTitleTV);
            bookSubtitleTV = (TextView) itemView.findViewById(R.id.bookSubtitleTV);
            bookIV = (ImageView) itemView.findViewById(R.id.bookIV);
            cartWishlistIV = (ImageView) itemView.findViewById(R.id.cartWishlistIV);
            cartWishlistIconHolder = (LinearLayout) itemView.findViewById(R.id.cartWishlistIconHolder);
        }
    }

    private void getSavedData() {
        cartBooks = new ArrayList<>();
        wishlistBooks = new ArrayList<>();
        String cartJSON = "";
        String wishListJSON = "";
        cartJSON = preferenceHelper.getUserInfo(Constants.CART_DATA) != null ? preferenceHelper.getUserInfo(Constants.CART_DATA) : "";
        if (cartJSON.length() > 0) {
            cartBooks = gson.fromJson(cartJSON, new TypeToken<List<Book>>() {
            }.getType());
        }
        wishListJSON = preferenceHelper.getUserInfo(Constants.WISHLIST_DATA) != null ? preferenceHelper.getUserInfo(Constants.WISHLIST_DATA) : "";
        if (wishListJSON.length() > 0) {
            wishlistBooks = gson.fromJson(wishListJSON, new TypeToken<List<Book>>() {
            }.getType());
        }
    }

    private int contains(Book book) {
        for(int i = 0; i < cartBooks.size(); i++) {
            if (book.getId() == cartBooks.get(i).getId()) {
                return CART;
            }
        }
        for(int i = 0; i < wishlistBooks.size(); i++) {
            if (book.getId() == wishlistBooks.get(i).getId()) {
                return WISHLIST;
            }
        }
        return NO_OPERATION;
    }
}
