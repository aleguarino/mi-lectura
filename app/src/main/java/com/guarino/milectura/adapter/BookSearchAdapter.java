/***************************************************
 * Autor: Alejandro Guarino Muñoz                  *
 *                                                 *
 * Descripcion: adaptador para el recicler         *
 * view que muestra la lista de libros encontrados *
 * a través de la query a la api de google books   *
 ***************************************************/
package com.guarino.milectura.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import com.guarino.milectura.R;
import com.guarino.milectura.data.model.Book;

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.ViewHolder> {
    private ArrayList<Book> list;
    private OnManageBookInfoListener listener;

    public BookSearchAdapter(OnManageBookInfoListener listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_book_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.book_title.setText(list.get(position).getTitle());
        holder.book_author.setText(list.get(position).getAuthor());
        holder.book_thumbnail.setImageURI(Uri.parse(list.get(position).getThumbnail()));
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView book_title;
        private TextView book_author;
        private SimpleDraweeView book_thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            book_title = itemView.findViewById(R.id.book_title);
            book_author = itemView.findViewById(R.id.book_author);
            book_thumbnail = itemView.findViewById(R.id.book_thumbnail);
        }

        public void bind(Book book, OnManageBookInfoListener listener) {
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
        this.list.addAll(list);
    }

    public interface OnManageBookInfoListener {
        void showBook(Book book);
    }
}
