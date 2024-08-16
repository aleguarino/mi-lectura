/****************************************************
 * Autor: Alejandro Guarino Muñoz                   *
 *                                                  *
 * Descripcion: repositorio sobre la entidad Goal   *
 ****************************************************/
package com.guarino.milectura.data.repository;

import java.util.concurrent.ExecutionException;

import com.guarino.milectura.data.dao.GoalDao;
import com.guarino.milectura.data.db.MiLecturaDatabase;
import com.guarino.milectura.data.model.Goal;

public class GoalRepository {

    private GoalDao goalDao;
    private static GoalRepository instance;

    static {
        instance = new GoalRepository();
    }

    private GoalRepository() {
        goalDao = MiLecturaDatabase.getDatabase().goalDao();
    }

    public static GoalRepository getInstance() {
        return instance;
    }

    public long insert(Goal goal) {
        long rowId = -1;
        try {
            rowId = MiLecturaDatabase.databaseWriteExecutor.submit(() -> goalDao.insert(goal)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return rowId;
        }
    }

    public Goal getGoal(String bookId) {
        Goal goal = null;
        try {
            goal = MiLecturaDatabase.databaseWriteExecutor.submit(() -> goalDao.getGoal(bookId)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return goal;
        }
    }

    public void update(Goal goal) {
        MiLecturaDatabase.databaseWriteExecutor.execute(() -> goalDao.update(goal));
    }

    public void delete(Goal goal) {
        MiLecturaDatabase.databaseWriteExecutor.execute(() -> goalDao.delete(goal));
    }

}
