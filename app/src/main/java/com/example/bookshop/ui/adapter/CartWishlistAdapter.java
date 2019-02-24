package com.example.bookshop.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookshop.R;
import com.example.bookshop.model.Book;

import java.util.List;

public class CartWishlistAdapter extends RecyclerView.Adapter<CartWishlistAdapter.ViewHolder> {

    private static final String TAG = CartWishlistAdapter.class.getSimpleName();

    private List<Book> books;
    private int rowLayout;
    private Context mContext;

    public CartWishlistAdapter(Context context, int rowLayout, List<Book> books) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.books = books;
    }

    @Override
    public CartWishlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new CartWishlistAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CartWishlistAdapter.ViewHolder holder, final int position) {

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
        public ImageView bookIV;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitleTV = (TextView) itemView.findViewById(R.id.bookTitleTV);
            bookSubtitleTV = (TextView) itemView.findViewById(R.id.bookSubtitleTV);
            bookIV = (ImageView) itemView.findViewById(R.id.bookIV);
        }
    }
}
