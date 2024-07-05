/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: fragment que muestra el listado de      *
 * libros que tienen una fecha límite establecida       *
 ********************************************************/
package com.example.milectura.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.milectura.R;
import com.example.milectura.adapter.GoalExpandableListAdapter;
import com.example.milectura.data.model.BooksWithGoal;


public class GoalsFragment extends Fragment implements GoalsContract.View {

    public static final String TAG = "GoalsFragment";
    private GoalsContract.Presenter presenter;
    private RelativeLayout rlNoData;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<BooksWithGoal>> expandableListDetail;

    public static Fragment newInstance(Bundle bundle) {
        GoalsFragment fragment = new GoalsFragment();
        if (bundle != null) fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandableListView = view.findViewById(R.id.expandableListView);
        rlNoData = view.findViewById(R.id.rlNoData);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.load();
    }

    @Override
    public void onSuccess(HashMap<String, List<BooksWithGoal>> map) {
        expandableListTitle = new ArrayList<String>(map.keySet());
        expandableListDetail = map;
        expandableListAdapter = new GoalExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        if (map.isEmpty())
            rlNoData.setVisibility(View.VISIBLE);
        else
            rlNoData.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(GoalsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public String setTitle(int id) {
        return getString(id);
    }

}