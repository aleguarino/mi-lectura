/***********************************************************
 * Autor: Alejandro Guarino Muñoz                          *
 *                                                         *
 * Descripcion: adaptador para la lista expandible         *
 * que muestra los libros con una fecha de finalización,   *
 * agrupando según se ha completado, está activo o fallido *
 ***********************************************************/
package com.guarino.milectura.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import com.guarino.milectura.R;
import com.guarino.milectura.data.model.BooksWithGoal;

public class GoalExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<BooksWithGoal>> expandableListDetail;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public GoalExpandableListAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<BooksWithGoal>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.goal_group, null);
        }
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setText(listTitle);
        ((ExpandableListView)parent).expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.cardview_item_book_goal, null);
        }
        TextView book_title = convertView.findViewById(R.id.book_title);
        TextView book_actual_page = convertView.findViewById(R.id.book_actual_page);
        TextView book_total_page = convertView.findViewById(R.id.book_total_page);
        TextView book_target_date = convertView.findViewById(R.id.book_target_date);
        SimpleDraweeView book_thumbnail = convertView.findViewById(R.id.book_thumbnail);
        book_title.setText(((BooksWithGoal)getChild(groupPosition, childPosition)).getBook().getTitle());
        book_actual_page.setText(String.valueOf(((BooksWithGoal)getChild(groupPosition, childPosition)).getBook().getActualPage()));
        book_total_page.setText(String.valueOf(((BooksWithGoal)getChild(groupPosition, childPosition)).getBook().getPages()));
        book_target_date.setText(sdf.format(((BooksWithGoal)getChild(groupPosition, childPosition)).getGoal().getTargetDate()));
        book_thumbnail.setImageURI(Uri.parse(((BooksWithGoal)getChild(groupPosition, childPosition)).getBook().getThumbnail()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
