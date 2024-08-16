/**********************************************************
 * Autor: Alejandro Guarino Muñoz                         *
 *                                                        *
 * Descripcion: presenter encargado de recoger el listado *
 * de libros con goals en la base de datos                *
 **********************************************************/
package com.guarino.milectura.ui.goals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.guarino.milectura.R;
import com.guarino.milectura.data.model.BooksWithGoal;
import com.guarino.milectura.data.repository.BookRepository;

public class GoalsPresenter implements GoalsContract.Presenter {

    private GoalsContract.View view;

    public GoalsPresenter(GoalsContract.View view) {
        this.view = view;
    }

    @Override
    public void load() {
        HashMap<String, List<BooksWithGoal>> expandableListDetail = new HashMap<>();
        List<BooksWithGoal> detail = BookRepository.getInstance().getBooksWithGoal();
        for (BooksWithGoal bg : detail) {
            String type;
            if (bg.getGoal().isCompleted()) {
                type = view.setTitle(R.string.completed);
            } else if (new Date().after(bg.getGoal().getTargetDate())) {
                type = view.setTitle(R.string.failed);
            } else {
                type = view.setTitle(R.string.active);
            }
            if (!expandableListDetail.containsKey(type)) {
                expandableListDetail.put(type, new ArrayList<BooksWithGoal>());
            }
            expandableListDetail.get(type).add(bg);
        }
        view.onSuccess(expandableListDetail);
    }
}
