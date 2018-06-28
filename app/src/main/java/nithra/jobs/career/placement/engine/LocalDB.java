package nithra.jobs.career.placement.engine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.utills.U;


/**
 * Created by ARUNRK THOMAS on 5/6/2017.
 */

public class LocalDB extends SQLiteOpenHelper {

    Context context;

    // Database Name
    public static final String DATABASE_NAME = "nithrajobs.db";
    public static final String KEY_ID = "id";
    public static final String KEY_JID = "jid";
    public static final String TABLE_JOB_BOOKMARK = "job_bookmark";
    public static final String TABLE_JOB_REMINDER = "job_reminder";

    public static final String KEY_TITLE = "title";
    public static final String KEY_EMP = "employer";
    public static final String KEY_DATE = "task_date";
    public static final String KEY_ACTION_DATE_TIME = "action_date";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_IS_AM_PM = "isampm";

    public final String READ_TABLE_NAME = "ReadUnreadTable";
    public final String READ_COLUMN_ID = "_id";
    public final String READ_COLUMN_JOB_ID = "read_id";

    // Current version of database
    private static final int DATABASE_VERSION = 2;
    public static final String CREATE_COURSE_BOOKMARK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_JOB_BOOKMARK + "("
            + KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_JID + " TEXT"
            + " );";
    public static final String CREATE_REMINDER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_JOB_REMINDER + "("
            + KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_JID+ " INTEGER,"
            + KEY_IMAGE + " TEXT,"
            + KEY_TITLE + " TEXT,"
            + KEY_EMP + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_ACTION_DATE_TIME + " INTEGER"
            + " );";

    public LocalDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSE_BOOKMARK);
        db.execSQL(CREATE_REMINDER);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + READ_TABLE_NAME + "(" + READ_COLUMN_ID + " integer primary key,"
                + READ_COLUMN_JOB_ID + " text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + READ_TABLE_NAME + "(" + READ_COLUMN_ID + " integer primary key,"
                    + READ_COLUMN_JOB_ID + " text )");
        }
    }

    public boolean addJobReminder(Jobs jobs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_JID, jobs.id);
        contentValues.put(KEY_IMAGE, jobs.image);
        contentValues.put(KEY_TITLE, jobs.jobtitle);
        contentValues.put(KEY_EMP, jobs.employer);
        contentValues.put(KEY_DATE, jobs.date);
        contentValues.put(KEY_ACTION_DATE_TIME, jobs.actionDate);
        long rowInserted = db.insert(TABLE_JOB_REMINDER, null, contentValues);
        db.close();
        return rowInserted != -1 ? true : false;
    }

    public boolean updateTask(Jobs jobs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_IMAGE, jobs.image);
        contentValues.put(KEY_TITLE, jobs.jobtitle);
        contentValues.put(KEY_EMP, jobs.employer);
        contentValues.put(KEY_DATE, jobs.date);
        contentValues.put(KEY_ACTION_DATE_TIME, jobs.actionDate);
        return db.update(TABLE_JOB_REMINDER, contentValues, KEY_JID+ " = " + jobs.id , null)>0;
    }

    public Jobs getItem(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        Jobs job = new Jobs();
        Cursor c;
        String query, title = "", emp = "", date = "", image = "";
        long actionDate = 0;
        query = "select * from " + TABLE_JOB_REMINDER + " where "+KEY_JID+" = " + id + "";

        c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                image = c.getString(c.getColumnIndex(KEY_IMAGE));
                title = c.getString(c.getColumnIndex(KEY_TITLE));
                emp = c.getString(c.getColumnIndex(KEY_EMP));
                date = c.getString(c.getColumnIndex(KEY_DATE));
                actionDate = c.getLong(c.getColumnIndex(KEY_ACTION_DATE_TIME));
                job.setId(id);
                job.setImage(image);
                job.setJobtitle(title);
                job.setEmployer(emp);
                job.setDate(date);
                job.setActionDate(actionDate);

            } while (c.moveToNext());
        }
        return job;
    }

    public List<Jobs> getReminders() {
        int vall =0;
        List<Jobs> list = new ArrayList<Jobs>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        String query = "select * from " + TABLE_JOB_REMINDER + " order by " + KEY_ID + " desc";
        c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                int id = 0, isAMPM = 0;
                String title = "", emp = "", date = "", image = "", verified;
                long actionDate = 0;

                image = c.getString(c.getColumnIndex(KEY_IMAGE));
                System.out.println("db image : " + image);
                id = c.getInt(c.getColumnIndex(KEY_JID));
                title = c.getString(c.getColumnIndex(KEY_TITLE));
                emp = c.getString(c.getColumnIndex(KEY_TITLE));
                date = c.getString(c.getColumnIndex(KEY_DATE));
                actionDate = c.getLong(c.getColumnIndex(KEY_ACTION_DATE_TIME));

                Jobs jobs = new Jobs();
                jobs.setId(id);
                jobs.setImage(image);
                jobs.setJobtitle(title);
                jobs.setEmployer(emp);
                jobs.setNoofvacancy(date);
                jobs.setActionDate(actionDate);

                /*if (list.size() % 5 == 0) jobs.setAd(true);
                else jobs.setAd(false);*/

                list.add(jobs);

            } while (c.moveToNext());
        }
        return list;
    }

    public boolean addJobBookMark(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_JID, id);
        long rowInserted = db.insert(TABLE_JOB_BOOKMARK, null, contentValues);
        db.close();
        return rowInserted != -1 ? true : false;
    }

    public boolean deleteJobBookMark(int id)  {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_JOB_BOOKMARK, KEY_JID + "=" + id, null) > 0;
    }

    public boolean deleteJobReminder(int id)  {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_JOB_REMINDER, KEY_JID + "=" + id, null) > 0;
    }

    public List<String> getBookMarks () {
        List<String> list = new ArrayList<String>();

        String selectQuery = "";
        selectQuery = "select * from " + TABLE_JOB_BOOKMARK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        if (c.moveToFirst()) {
            do {
                int id = 0;
                id = c.getInt(c.getColumnIndex(KEY_JID));
                list.add(id+"");
            } while (c.moveToNext());
        }
        return list;
    }

    public boolean isBookMarkExists(int id) {

        String selectQuery = "";
        selectQuery = "select * from " + TABLE_JOB_BOOKMARK + " where " + KEY_JID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        if (c.moveToFirst()) return true;
        else return false;
    }

    public boolean isReminderExists(int id) {
        String selectQuery = "";
        selectQuery = "select * from " + TABLE_JOB_REMINDER + " where " + KEY_JID + " = " + id;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = this.getReadableDatabase();
            c = db.rawQuery(selectQuery, null);
            U.PQ(selectQuery+"--"+c.getCount());
            if (c.getCount()!=0) {
                return true;
            } else {
                return false;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
            
        } finally {
            c.close();
            db.close();
        }
    }

    public Cursor getQry(String Qry) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(Qry, null);
    }

    public void executeSql(String Qry) {
        SQLiteDatabase sq = this.getReadableDatabase();
        sq.execSQL(Qry);
    }

    public boolean insertReadId(String newsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(READ_COLUMN_JOB_ID, newsId);
        db.insert(READ_TABLE_NAME, null, values);
        return true;
    }

}
