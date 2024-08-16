/*******************************************************
 * Autor: Alejandro Guarino Muñoz                      *
 *                                                     *
 * Descripcion: adaptador para el recicler             *
 * view que muestra la lista de libros que el usuario  *
 * ha agregado a su biblioteca                         *
 *******************************************************/
package com.guarino.milectura.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.guarino.milectura.R;
import com.guarino.milectura.data.model.Book;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private ArrayList<Book> list;
    private ViewPager2 viewPager2;
    private OnManageLibraryListener listener;

    public LibraryAdapter(ViewPager2 viewPager2, OnManageLibraryListener listener) {
        this.list = new ArrayList<>();
        this.viewPager2 = viewPager2;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.book_title.setText(list.get(position).getTitle());
        holder.book_thumbnail.setImageURI(Uri.parse(list.get(position).getThumbnail()));
        holder.bind(list.get(position), listener);
        //Picasso.get().load(list.get(position).getThumbnail()).into(holder.book_thumbnail);
        /*final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(list.get(position).getThumbnail()))
                        .build();
        holder.book_thumbnail.setImageRequest(imageRequest);*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView book_title;
        private SimpleDraweeView book_thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            book_title = itemView.findViewById(R.id.book_title);
            book_thumbnail = itemView.findViewById(R.id.book_thumbnail);
        }


        public void bind(Book book, OnManageLibraryListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.showBook(book);
                }
            });
        }
    }

    public void clear() {
        list.clear();
    }

    public void load(List<Book> list) {
        Collections.sort(list);
        this.list.addAll(list);
    }

    public void sort() {
        Collections.sort(this.list);
    }

    public int size() {
        return this.list.size();
    }

    public Book getBook(int position) {
        return this.list.get(position);
    }

    public interface OnManageLibraryListener {
        void showBook(Book book);
    }
}
