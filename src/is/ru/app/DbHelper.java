package is.ru.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Þórður on 23.9.2014.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "FLOW_DB";
    public static final int DB_VERSION = 1;

    public static final String TableFlow = "flow";
    public static final String[] TableFlowCols = { "_id", "boardId", "type", "bestTime", "finished" };

    private static final String sqlCreateTableFlow =
            "CREATE TABLE flow(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " boardId INTEGER NOT NULL," +
                    " type TEXT NOT NULL," +
                    " bestTime DATETIME," +
                    " finished BOOLEAN DEFAULT FALSE " +
                    ");";

    private static final String sqlDropTableFlow =
            "DROP TABLE IF EXISTS flow;";

    public DbHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( sqlCreateTableFlow );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( sqlDropTableFlow );
        onCreate( db );
    }
}
