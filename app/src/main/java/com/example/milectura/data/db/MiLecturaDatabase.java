/**********************************************************
 * Autor: Alejandro Guarino Muñoz                         *
 *                                                        *
 * Descripcion: clase que genera la base de datos local   *
 **********************************************************/
package com.example.milectura.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.milectura.data.dao.BookDao;
import com.example.milectura.data.dao.GoalDao;
import com.example.milectura.data.dao.ReadingDao;
import com.example.milectura.data.model.Book;
import com.example.milectura.data.model.Goal;
import com.example.milectura.data.model.Reading;

@Database(entities = {Book.class, Reading.class, Goal.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class MiLecturaDatabase extends RoomDatabase {

    public abstract BookDao bookDao();
    public abstract ReadingDao readingDao();
    public abstract GoalDao goalDao();

    private static volatile MiLecturaDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static void create(final Context context) {
        if (INSTANCE == null) {
            synchronized (MiLecturaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MiLecturaDatabase.class, "milecturadatabase")
                            .build();
                }
            }
        }
        //populateData();
    }

    private static void populateData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Book book = new Book("840135403X",
                "It",
                "Stephen King",
                1504,
                1504,
                "https://books.google.com/books/content?id=HK-gnkponNIC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                "¿Quién o qué mutila y mata a los niños de un pequeño pueblo norteamericano? ¿Por qué llega cíclicamente el horror a Derry en forma de un payaso siniestro que va sembrando la destrucción a su paso? Esto es lo que se proponen averiguar los protagonistas de esta novela. Tras veintisiete años de tranquilidad y lejanía, una antigua promesa infantil les hace volver al lugar en el que vivieron su infancia y juventud como una terrible pesadilla. Regresan a Derry para enfrentarse con su pasado y enterrar definitivamente la amenaza que los amargó durante su niñez. Saben que pueden morir, pero son conscientes de que no conocerán la paz hasta que aquella cosa sea destruida para siempre. It es una de las novelas más ambiciosas de Stephen King, con la que ha logrado perfeccionar de un modo muy personal las claves del género de terror. La crítica ha dicho... «Insuperable.» La Vanguardia",
                new Date());
        Book book2 = new Book("8490325073",
                "1984",
                "George Orwell",
                354,
                0,
                "https://books.google.com/books/content?id=uFI8Kmx3a0oC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                "«No creo que la sociedad que he descrito en 1984 necesariamente llegue a ser una realidad, pero sí creo que puede llegar a existir algo parecido», escribía Orwell después de publicar su novela. Corría el año 1948, y la realidad se ha encargado de convertir esa pieza -entonces de ciencia ficción- en un manifiesto de la realidad. En el año 1984 Londres es una ciudad lúgubre en la que la Policía del Pensamiento controla de forma asfixiante la vida de los ciudadanos. Winston Smith es un peón de este engranaje perverso y su cometido es reescribir la historia para adaptarla a lo que el Partido considera la versión oficial de los hechos. Hasta que decide replantearse la verdad del sistema que los gobierna y somete. La crítica ha dicho... «Aquí ya no estamos solo ante lo que habitualmente reconocemos como \"literatura\" e identificamos con la buena escritura. Aquí estamos, repito, ante energía visionaria. Y no todas las visiones se refieren al futuro, o al Más Allá.» Umberto Eco «Entre mis libros favoritos, lo leo una y otra vez.» Margaret Atwood «No es difícil pensar que Orwell, en 1984, estuviera imaginando un futuro para la generación de su hijo, un mundo del que deseaba prevenirles.» Thomas Pynchon «La libertad es una obligación tan dolorosa que siempre habrá quien prefiera rendirse. La virtud de libros como 1984 es su capacidad para recordarnos que la libertad de los seres humanos responsables no es igual a la de los animales.» Anthony Burgess «Desde El proceso de Kafka ninguna obra fantástica ha alcanzado el horror lógico de 1984.» Arthur Koestler «Un libro magnífico y profundamente interesante.» Aldous Huxley",
                new Date());
        Book book3 = new Book("8446032430",
                "Drácula",
                "Jorge Martínez Juárez",
                144,
                0,
                "https://books.google.com/books/content?id=VEybsPfD4J8C&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                "Relato de terror que recrea la historia de Vlad Tepes y la novela de Bram Stoker adaptado ahora para jóvenes lectores. Este texto está ilustrado y resulta muy apropiado para jóvenes de edades comprendidas entre once y catorce años",
                new Date());
        databaseWriteExecutor.execute(() -> {
            BookDao bookDao = INSTANCE.bookDao();
            ReadingDao readingDao = INSTANCE.readingDao();
            bookDao.deleteAll();
            bookDao.insert(book);
            readingDao.deleteAll();
        });
    }

    public static MiLecturaDatabase getDatabase() {
        return INSTANCE;
    }
}
