package is.ru.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;

/**
 * Created by Þórður on 23.9.2014.
 */
public class FlowDbAdapter {

    SQLiteDatabase db;
    DbHelper dbHelper;
    Context context;


    public FlowDbAdapter( Context c ) {
        context = c;
    }

    public FlowDbAdapter openToRead() {
        dbHelper = new DbHelper( context );
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public FlowDbAdapter openToWrite() {
        dbHelper = new DbHelper( context );
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public long insertFlow( int boardId, Timestamp bestTime, String type, boolean finished ) {
        String[] cols = DbHelper.TableFlowCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)boardId).toString() );
        contentValues.put( cols[2], type);
        contentValues.put( cols[3], String.valueOf(bestTime));
        contentValues.put( cols[4], finished);
        openToWrite();
        long value = db.insert(DbHelper.TableFlow, null, contentValues );
        close();
        return value;
    }

    public long updateFlow( int boardId, Timestamp bestTime, String type, boolean finished ) {
        String[] cols = DbHelper.TableFlowCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)boardId).toString() );
        contentValues.put( cols[2], type);
        contentValues.put( cols[3], String.valueOf(bestTime));
        contentValues.put( cols[4], finished);
        openToWrite();
        long value = db.update(DbHelper.TableFlow,
                contentValues,
                cols[1] + "=" + boardId, null );
        close();
        return value;
    }

    public long updateFlowFinished( int id, String type, boolean finished ) {
        String[] cols = DbHelper.TableFlowCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[4], finished);
        openToWrite();
        long value = db.update(DbHelper.TableFlow,
                contentValues,
                "type LIKE '" + type + "' AND boardId = " + id, null );
        close();
        return value;
    }


    public Cursor queryFlows(int id, String type) {
        openToRead();
        Cursor cursor = db.query( DbHelper.TableFlow,
                DbHelper.TableFlowCols, "type LIKE '" + type + "' AND boardId = " + id, null  , null, null, null);
        return cursor;
    }

}
