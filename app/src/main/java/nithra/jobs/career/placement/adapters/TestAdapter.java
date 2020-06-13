package nithra.jobs.career.placement.adapters;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

import nithra.jobs.career.placement.engine.DBHelper;

public class TestAdapter {
    protected static final String TAG = "DataAdapter";
    private final Context mContext;
    private SQLiteDatabase mDb;
    private DBHelper mDbHelper;

    public TestAdapter(Context paramContext) {
        this.mContext = paramContext;
        this.mDbHelper = new DBHelper(this.mContext);
    }

    public void close() {
        this.mDbHelper.close();
    }

    public TestAdapter createDatabase()
            throws SQLException {
        try {
            this.mDbHelper.createDataBase();
            return this;
        } catch (IOException localIOException) {
            Log.e("DataAdapter", localIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
    }

    public TestAdapter open()
            throws SQLException {
        try {
            this.mDbHelper.openDataBase();
            this.mDbHelper.close();
            this.mDb = this.mDbHelper.getReadableDatabase();
            return this;
        } catch (SQLException localSQLException) {
            Log.e("DataAdapter", "open >>" + localSQLException.toString());
            throw localSQLException;
        }
    }
}