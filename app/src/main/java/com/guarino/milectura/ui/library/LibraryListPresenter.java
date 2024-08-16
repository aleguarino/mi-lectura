/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: presenter que obtiene el listado de los *
 * libros del usuario de la base de datos, así como     *
 * recoger y actualizar su progreso de lectura          *
 ********************************************************/
package com.guarino.milectura.ui.library;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.guarino.milectura.data.model.Book;
import com.guarino.milectura.data.model.Goal;
import com.guarino.milectura.data.model.Reading;
import com.guarino.milectura.data.repository.BookRepository;
import com.guarino.milectura.data.repository.GoalRepository;
import com.guarino.milectura.data.repository.ReadingRepository;

public class LibraryListPresenter implements LibraryListContract.Presenter {

    private LibraryListContract.View view;

    public LibraryListPresenter(LibraryListContract.View view) {
        this.view = view;
    }

    @Override
    public void load() {
        List<Book> list = BookRepository.getInstance().getList();
        if (list == null) {
            list = new ArrayList<>();
        }
        view.onSuccess(list);
    }

    @Override
    public int getProgress(String bookId) {
        return BookRepository.getInstance().getActualPageFromBook(bookId);
    }

    @Override
    public void updateProgress(Book book, Date date, int page, boolean finished) {
        book.setModifiedAt(System.currentTimeMillis());
        int readed = 0;
        int actualPage = BookRepository.getInstance().getActualPageFromBook(book.getIsbn());
        int totalPage = BookRepository.getInstance().getPages(book.getIsbn());
        if (!finished)
            readed = page - actualPage;
        else {
            page = totalPage;
            readed = totalPage - actualPage;
        }
        finished = totalPage == page;
        ReadingRepository.getInstance().insert(new Reading(book.getIsbn(), date, readed));
        BookRepository.getInstance().updateActualPage(book.getIsbn(), page, finished, book.getModifiedAt());
        Goal goal = getGoal(book.getIsbn());
        if (goal != null) {
            int remainingPages = goal.getRemainingPages() - readed;
            boolean zeroPagesLeft = false;
            if (remainingPages == 0)
                zeroPagesLeft = true;
            if (new Date().before(goal.getTargetDate()) && zeroPagesLeft)
                goal.setCompleted(true);
            goal.setRemainingPages(remainingPages);
            GoalRepository.getInstance().update(goal);
        }
        view.updateProgress(page);
    }

    private Goal getGoal(String isbn) {
        return GoalRepository.getInstance().getGoal(isbn);
    }
}