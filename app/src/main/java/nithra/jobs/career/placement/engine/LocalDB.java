package nithra.jobs.career.placement.engine;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nithra.jobs.career.placement.pojo.Filter;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.pojo.Notifications;
import nithra.jobs.career.placement.pojo.PostDraftJob;
import nithra.jobs.career.placement.utills.U;


/**
 * Created by ARUNRK THOMAS on 5/6/2017.
 */

public class LocalDB extends SQLiteOpenHelper {

    public static final String TABLE_EMPLOYEE_SHORTLIST = "employee_shortlist";
    // Database Name
    private static final String DATABASE_NAME = "nithrajobs.db";
    private static final String KEY_ID = "id";
    private static final String KEY_JID = "jid";
    private static final String TABLE_JOB_BOOKMARK = "job_bookmark";
    private static final String TABLE_JOB_REMINDER = "job_reminder";
    private static final String TABLE_SAVED_FILTER = "job_saved_filter";
    private static final String TABLE_SAVED_NOTIFICATION = "job_saved_notification";
    private static final String TABLE_DRAFT_JOBS = "draft_jobs";
    private static final String TABLE_EMPLOYEE_BOOKMARK = "employee_bookmark";
    private static final String TABLE_APPLIED_JOBS = "applied_jobs";

    private static final String KEY_TITLE = "title";
    private static final String KEY_EMP = "employer";
    private static final String KEY_DATE = "task_date";
    private static final String KEY_ACTION_DATE_TIME = "action_date";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_IS_AM_PM = "isampm";
    private static final String KEY_TITLE_ID = "title_id";

    private static final String READ_TABLE_NAME = "ReadUnreadTable";
    private static final String READ_COLUMN_ID = "_id";
    private static final String READ_COLUMN_JOB_ID = "read_id";

    private static final String KEY_FILTER_NAME = "KEY_FILTER_NAME";
    private static final String KEY_GENDER = "KEY_GENDER";
    private static final String KEY_MARITAL_STATUS = "KEY_MARITAL_STATUS";
    private static final String KEY_START_AGE = "KEY_START_AGE";
    private static final String KEY_END_AGE = "KEY_END_AGE";
    private static final String KEY_SKILLS = "KEY_SKILLS";
    private static final String KEY_QUALIFICATION = "KEY_QUALIFICATION";
    private static final String KEY_LOCATION = "KEY_LOCATION";

    private static final String SAVE_NOTI_TITLE = "title";
    private static final String SAVE_NOTI_DATE = "date";
    private static final String SAVE_NOTI_MESSAGE = "message";
    private static final String SAVE_NOTI_TIME = "time";
    private static final String SAVE_NOTI_IS_CLOSE = "isclose";
    private static final String SAVE_NOTI_TYPE = "type";
    private static final String SAVE_NOTI_BM = "bm";
    private static final String SAVE_NOTI_NTYPE = "ntype";
    private static final String SAVE_NOTI_URL = "url";

    private static final String DRAFT_JOBTITILE_ID = "DRAFT_JOBTITILE_ID";
    private static final String DRAFT_JOBTITILE = "DRAFT_JOBTITILE";
    private static final String DRAFT_COMPANY_ADDRESS = "DRAFT_COMPANY_ADDRESS";
    private static final String DRAFT_JOBLOCATION = "DRAFT_JOBLOCATION";
    private static final String DRAFT_CANDIDATE_LOCATION = "DRAFT_CANDIDATE_LOCATION";
    private static final String DRAFT_MOBILE_NUMBER = "DRAFT_MOBILE_NUMBER";
    private static final String DRAFT_EMAIL = "DRAFT_EMAIL";
    private static final String DRAFT_MARITALSTATUS = "DRAFT_MARITALSTATUS";
    private static final String DRAFT_GENDER = "DRAFT_GENDER";
    private static final String DRAFT_WEBSITE = "DRAFT_WEBSITE";
    private static final String DRAFT_WORKMODE = "DRAFT_WORKMODE";
    private static final String DRAFT_DESCRIPTION = "DRAFT_DESCRIPTION";
    private static final String DRAFT_SKILLS = "DRAFT_SKILLS";
    private static final String DRAFT_SINGLE_QUALIFICATION = "DRAFT_SINGLE_QUALIFICATION";
    private static final String DRAFT_GROUP_QUALIFICATION = "DRAFT_GROUP_QUALIFICATION";
    private static final String DRAFT_VACANCY = "DRAFT_VACANCY";
    private static final String DRAFT_EXPERIENCE = "DRAFT_EXPERIENCE";
    private static final String DRAFT_AGELIMIT = "DRAFT_AGELIMIT";
    private static final String DRAFT_SALARY = "DRAFT_SALARY";
    private static final String DRAFT_KEYWORD = "DRAFT_KEYWORD";
    private static final String DRAFT_LATLON = "DRAFT_LATLON";
    private static final String DRAFT_CALLOPTION = "DRAFT_CALLOPTION";
    private static final String DRAFT_MAILOPTION = "DRAFT_MAILOPTION";
    private static final String DRAFT_MAIL_WITH_RESUME = "DRAFT_MAIL_WITH_RESUME";


    // Current version of database
    private static final int DATABASE_VERSION = 5;

    private static final String CREATE_COURSE_BOOKMARK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_JOB_BOOKMARK + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_JID + " TEXT,"
            + KEY_TITLE_ID + " TEXT"
            + " );";

    private static final String CREATE_APPLIED_JOBS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_APPLIED_JOBS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_JID + " TEXT,"
            + KEY_TITLE_ID + " TEXT"
            + " );";

    private static final String CREATE_EMPLOYEE_BOOKMARK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EMPLOYEE_BOOKMARK + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_JID + " TEXT"
            + " );";

    private static final String CREATE_EMPLOYEE_SHORTLIST = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EMPLOYEE_SHORTLIST + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_JID + " TEXT"
            + " );";

    private static final String CREATE_READ_JOBS = "CREATE TABLE IF NOT EXISTS "
            + READ_TABLE_NAME + "("
            + READ_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + READ_COLUMN_JOB_ID + " TEXT,"
            + KEY_TITLE_ID + " TEXT"
            + " );";

    private static final String CREATE_REMINDER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_JOB_REMINDER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_JID + " INTEGER,"
            + KEY_IMAGE + " TEXT,"
            + KEY_TITLE + " TEXT,"
            + KEY_TITLE_ID + " TEXT,"
            + KEY_EMP + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_ACTION_DATE_TIME + " INTEGER"
            + " );";

    private static final String CREATE_SAVED_FILTER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SAVED_FILTER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FILTER_NAME + " TEXT,"
            + KEY_GENDER + " TEXT,"
            + KEY_MARITAL_STATUS + " TEXT,"
            + KEY_START_AGE + " TEXT,"
            + KEY_END_AGE + " TEXT,"
            + KEY_SKILLS + " TEXT,"
            + KEY_QUALIFICATION + " TEXT,"
            + KEY_LOCATION + " TEXT,"
            + KEY_ACTION_DATE_TIME + " INTEGER"
            + " );";

    private static final String CREATE_SAVED_NOTIFICATION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SAVED_NOTIFICATION + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_JID + " INTEGER ,"
            + SAVE_NOTI_TITLE + " TEXT,"
            + SAVE_NOTI_MESSAGE + " TEXT,"
            + SAVE_NOTI_DATE + " TEXT,"
            + SAVE_NOTI_TIME + " TEXT,"
            + SAVE_NOTI_IS_CLOSE + " TEXT,"
            + SAVE_NOTI_TYPE + " TEXT,"
            + SAVE_NOTI_BM + " TEXT,"
            + SAVE_NOTI_NTYPE + " TEXT,"
            + SAVE_NOTI_URL + " TEXT"
            + " );";

    private static final String CREATE_DRAFT_JOB = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DRAFT_JOBS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ACTION_DATE_TIME + " INTEGER ,"
            + DRAFT_JOBTITILE_ID + " TEXT ,"
            + DRAFT_JOBTITILE + " TEXT ,"
            + DRAFT_COMPANY_ADDRESS + " TEXT ,"
            + DRAFT_JOBLOCATION + " TEXT ,"
            + DRAFT_CANDIDATE_LOCATION + " TEXT ,"
            + DRAFT_MOBILE_NUMBER + " TEXT ,"
            + DRAFT_EMAIL + " TEXT ,"
            + DRAFT_MARITALSTATUS + " TEXT ,"
            + DRAFT_GENDER + " TEXT ,"
            + DRAFT_WEBSITE + " TEXT ,"
            + DRAFT_WORKMODE + " TEXT ,"
            + DRAFT_DESCRIPTION + " TEXT ,"
            + DRAFT_SKILLS + " TEXT ,"
            + DRAFT_SINGLE_QUALIFICATION + " TEXT ,"
            + DRAFT_GROUP_QUALIFICATION + " TEXT ,"
            + DRAFT_VACANCY + " TEXT ,"
            + DRAFT_EXPERIENCE + " TEXT ,"
            + DRAFT_AGELIMIT + " TEXT ,"
            + DRAFT_SALARY + " TEXT ,"
            + DRAFT_KEYWORD + " TEXT ,"
            + DRAFT_LATLON + " TEXT ,"
            + DRAFT_CALLOPTION + " TEXT ,"
            + DRAFT_MAILOPTION + " TEXT ,"
            + DRAFT_MAIL_WITH_RESUME + " TEXT"
            + " );";

    public LocalDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSE_BOOKMARK);
        db.execSQL(CREATE_REMINDER);
        db.execSQL(CREATE_EMPLOYEE_BOOKMARK);
        db.execSQL(CREATE_EMPLOYEE_SHORTLIST);
        db.execSQL(CREATE_SAVED_FILTER);
        db.execSQL(CREATE_SAVED_NOTIFICATION);
        db.execSQL(CREATE_DRAFT_JOB);
        db.execSQL(CREATE_READ_JOBS);
        db.execSQL(CREATE_APPLIED_JOBS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.e("update", "called");
            db.execSQL(CREATE_EMPLOYEE_BOOKMARK);
            db.execSQL(CREATE_EMPLOYEE_SHORTLIST);
            db.execSQL(CREATE_SAVED_FILTER);
            db.execSQL(CREATE_SAVED_NOTIFICATION);
            db.execSQL(CREATE_DRAFT_JOB);
            db.execSQL(CREATE_READ_JOBS);
            db.execSQL(CREATE_APPLIED_JOBS);

            try {
                db.execSQL("ALTER TABLE " + READ_TABLE_NAME + " ADD COLUMN " + KEY_TITLE_ID + " TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE " + TABLE_JOB_BOOKMARK + " ADD COLUMN " + KEY_TITLE_ID + " TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE " + TABLE_JOB_REMINDER + " ADD COLUMN " + KEY_TITLE_ID + " TEXT DEFAULT ''");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addJobReminder(Jobs jobs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_JID, jobs.id);
        contentValues.put(KEY_IMAGE, jobs.image);
        contentValues.put(KEY_TITLE, jobs.jobtitle);
        contentValues.put(KEY_TITLE_ID, jobs.jobtitle);
        contentValues.put(KEY_EMP, jobs.employer);
        contentValues.put(KEY_DATE, jobs.date);
        contentValues.put(KEY_ACTION_DATE_TIME, jobs.actionDate);
        long rowInserted = db.insert(TABLE_JOB_REMINDER, null, contentValues);
        db.close();
        return rowInserted != -1;
    }

    public boolean updateTask(Jobs jobs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_IMAGE, jobs.image);
        contentValues.put(KEY_TITLE, jobs.jobtitle);
        contentValues.put(KEY_EMP, jobs.employer);
        contentValues.put(KEY_DATE, jobs.date);
        contentValues.put(KEY_ACTION_DATE_TIME, jobs.actionDate);
        return db.update(TABLE_JOB_REMINDER, contentValues, KEY_JID + " = " + jobs.id, null) > 0;
    }

    public Jobs getItem(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Jobs job = new Jobs();
        Cursor c;
        String query, title, titleId, emp, date, image;
        long actionDate = 0;
        query = "select * from " + TABLE_JOB_REMINDER + " where " + KEY_JID + " = " + id + "";

        c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                image = c.getString(c.getColumnIndex(KEY_IMAGE));
                title = c.getString(c.getColumnIndex(KEY_TITLE));
                titleId = c.getString(c.getColumnIndex(KEY_TITLE_ID));
                emp = c.getString(c.getColumnIndex(KEY_EMP));
                date = c.getString(c.getColumnIndex(KEY_DATE));
                actionDate = c.getLong(c.getColumnIndex(KEY_ACTION_DATE_TIME));
                job.setId(id);
                job.setImage(image);
                job.setJobtitle(title);
                job.setJobtitle(titleId);
                job.setEmployer(emp);
                job.setDate(date);
                job.setActionDate(actionDate);

            } while (c.moveToNext());
        }
        return job;
    }

    public List<Jobs> getReminders() {
        int vall = 0;
        List<Jobs> list = new ArrayList<Jobs>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        String query = "select * from " + TABLE_JOB_REMINDER + " order by " + KEY_ID + " desc";
        c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                int id = 0, isAMPM = 0;
                String title = "", titleId = "", emp = "", date = "", image = "", verified;
                long actionDate = 0;

                image = c.getString(c.getColumnIndex(KEY_IMAGE));
                System.out.println("db image : " + image);
                id = c.getInt(c.getColumnIndex(KEY_JID));
                title = c.getString(c.getColumnIndex(KEY_TITLE));
                titleId = c.getString(c.getColumnIndex(KEY_TITLE_ID));
                emp = c.getString(c.getColumnIndex(KEY_TITLE));
                date = c.getString(c.getColumnIndex(KEY_DATE));
                actionDate = c.getLong(c.getColumnIndex(KEY_ACTION_DATE_TIME));

                Jobs jobs = new Jobs();
                jobs.setId(id);
                jobs.setImage(image);
                jobs.setJobtitle(title);
//                jobs.setJobtitle(titleId);
                jobs.setEmployer(emp);
                jobs.setNoofvacancy(date);
                jobs.setActionDate(actionDate);

                list.add(jobs);

            } while (c.moveToNext());
        }
        return list;
    }

    public boolean addJobBookMark(int id, String titleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_JID, id);
        contentValues.put(KEY_TITLE_ID, titleId);
        long rowInserted = db.insert(TABLE_JOB_BOOKMARK, null, contentValues);
        db.close();
        return rowInserted != -1;
    }

    public boolean deleteJobBookMark(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_JOB_BOOKMARK, KEY_JID + "=" + id, null) > 0;
    }

    public boolean deleteJobReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_JOB_REMINDER, KEY_JID + "=" + id, null) > 0;
    }

    public List<String> getEmployeeBookMarks() {
        List<String> list = new ArrayList<String>();

        String selectQuery = "";
        selectQuery = "select * from " + TABLE_EMPLOYEE_BOOKMARK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        if (c.moveToFirst()) {
            do {
                int id = 0;
                id = c.getInt(c.getColumnIndex(KEY_JID));
                list.add(id + "");
            } while (c.moveToNext());
        }
        return list;
    }

    public List<String> getEmployeeShortlists() {
        List<String> list = new ArrayList<String>();

        String selectQuery = "";
        selectQuery = "select * from " + TABLE_EMPLOYEE_SHORTLIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        if (c.moveToFirst()) {
            do {
                int id = 0;
                id = c.getInt(c.getColumnIndex(KEY_JID));
                list.add(id + "");
            } while (c.moveToNext());
        }
        return list;
    }

    public boolean isBookMarkExists(int id) {
        String selectQuery = "";
        selectQuery = "select * from " + TABLE_JOB_BOOKMARK + " where " + KEY_JID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;

        try {
            c = db.rawQuery(selectQuery, null);
            U.PQ(selectQuery);
            boolean a = c.moveToFirst();
            c.close();
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (c != null) {
                c.close();
                db.close();
            }
        }
    }

    public boolean isBookMarkTitleExists(int id) {
        String selectQuery = "";
        selectQuery = "select * from " + TABLE_JOB_BOOKMARK + " where " + KEY_TITLE_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(selectQuery, null);
            U.PQ(selectQuery);
            boolean a = c.moveToFirst();
            c.close();
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (c != null) {
                c.close();
                db.close();
            }
        }
    }

    public boolean isReminderExists(int id) {
        String selectQuery = "";
        selectQuery = "select * from " + TABLE_JOB_REMINDER + " where " + KEY_JID + " = " + id;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = this.getReadableDatabase();
            c = db.rawQuery(selectQuery, null);
            U.PQ(selectQuery + "--" + c.getCount());
            return c.getCount() != 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            if (c != null) {
                c.close();
                db.close();
            }
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

    public List<String> getBookMarks() {
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
                list.add(id + "");
            } while (c.moveToNext());
        }
        return list;
    }

    public boolean addEmployeeBookMark(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_JID, id);
        long rowInserted = db.insert(TABLE_EMPLOYEE_BOOKMARK, null, contentValues);
        db.close();
        return rowInserted != -1;
    }

    public boolean deleteEmployeeBookMark(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EMPLOYEE_BOOKMARK, KEY_JID + "=" + id, null) > 0;
    }

    public boolean isEmployeeBookMarkExists(int id) {

        String selectQuery = "";
        selectQuery = "select * from " + TABLE_EMPLOYEE_BOOKMARK + " where " + KEY_JID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        return c.moveToFirst();
    }

    public boolean addEmployeeShortlist(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_JID, id);
        long rowInserted = db.insert(TABLE_EMPLOYEE_SHORTLIST, null, contentValues);
        db.close();
        return rowInserted != -1;
    }

    public boolean deleteEmployeeShortlist(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EMPLOYEE_SHORTLIST, KEY_JID + "=" + id, null) > 0;
    }

    public boolean isEmployeeShortlistExists(int id) {
        String selectQuery = "";
        selectQuery = "select * from " + TABLE_EMPLOYEE_SHORTLIST + " where " + KEY_JID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        return c.moveToFirst();
    }

    public boolean addFilter(Filter filter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FILTER_NAME, filter.name);
        contentValues.put(KEY_GENDER, filter.gender);
        contentValues.put(KEY_MARITAL_STATUS, filter.maritalStatus);
        contentValues.put(KEY_START_AGE, filter.startAge);
        contentValues.put(KEY_END_AGE, filter.endAge);
        contentValues.put(KEY_SKILLS, filter.skills);
        contentValues.put(KEY_QUALIFICATION, filter.qualification);
        contentValues.put(KEY_LOCATION, filter.location);
        contentValues.put(KEY_ACTION_DATE_TIME, filter.actionDate);
        long rowInserted = db.insert(TABLE_SAVED_FILTER, null, contentValues);
        db.close();
        return rowInserted != -1;
    }

    public boolean updateFilter(Filter filter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FILTER_NAME, filter.name);
        contentValues.put(KEY_GENDER, filter.gender);
        contentValues.put(KEY_MARITAL_STATUS, filter.maritalStatus);
        contentValues.put(KEY_START_AGE, filter.startAge);
        contentValues.put(KEY_END_AGE, filter.endAge);
        contentValues.put(KEY_SKILLS, filter.skills);
        contentValues.put(KEY_QUALIFICATION, filter.qualification);
        contentValues.put(KEY_LOCATION, filter.location);
        contentValues.put(KEY_ACTION_DATE_TIME, filter.actionDate);
        return db.update(TABLE_SAVED_FILTER, contentValues, KEY_FILTER_NAME + " = " + "'" + filter.name + "'", null) > 0;
    }

    public void deleteFilter(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SAVED_FILTER, KEY_ID + "=" + id, null);
    }

    public List<Filter> getAllFilters() {
        List<Filter> list = new ArrayList<>();

        String selectQuery = "";
        selectQuery = "select * from " + TABLE_SAVED_FILTER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        if (c.moveToFirst()) {
            do {
                int id = 0;
                String filtername = "", gender = "", maritalStatus = "", startAge = "", endAge = "",
                        skills = "", qualification = "", location = "";
                long actionDate = 0;

                id = c.getInt(c.getColumnIndex(KEY_ID));
                filtername = c.getString(c.getColumnIndex(KEY_FILTER_NAME));
                gender = c.getString(c.getColumnIndex(KEY_GENDER));
                maritalStatus = c.getString(c.getColumnIndex(KEY_MARITAL_STATUS));
                startAge = c.getString(c.getColumnIndex(KEY_START_AGE));
                endAge = c.getString(c.getColumnIndex(KEY_END_AGE));
                skills = c.getString(c.getColumnIndex(KEY_SKILLS));
                qualification = c.getString(c.getColumnIndex(KEY_QUALIFICATION));
                location = c.getString(c.getColumnIndex(KEY_LOCATION));
                actionDate = c.getLong(c.getColumnIndex(KEY_ACTION_DATE_TIME));

                Filter filter = new Filter();
                filter.setId(id);
                filter.setName(filtername);
                filter.setGender(gender);
                filter.setMaritalStatus(maritalStatus);
                filter.setStartAge(startAge);
                filter.setEndAge(endAge);
                filter.setSkills(skills);
                filter.setQualification(qualification);
                filter.setLocation(location);
                filter.setActionDate(actionDate);

                list.add(filter);

            } while (c.moveToNext());
        }
        return list;
    }

    public boolean isFilterExists(String name) {
        String selectQuery = "";
        selectQuery = "select * from " + TABLE_SAVED_FILTER + " where " + KEY_FILTER_NAME + " = " + "'" + name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return c.moveToFirst();
    }

    public boolean saveNotification(int id, String title, String message, String date, String time, String isClose, String type, String bm, String ntype, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_JID, id);
        contentValues.put(SAVE_NOTI_TITLE, title);
        contentValues.put(SAVE_NOTI_MESSAGE, message);
        contentValues.put(SAVE_NOTI_DATE, date);
        contentValues.put(SAVE_NOTI_TIME, time);
        contentValues.put(SAVE_NOTI_IS_CLOSE, isClose);
        contentValues.put(SAVE_NOTI_TYPE, type);
        contentValues.put(SAVE_NOTI_BM, bm);
        contentValues.put(SAVE_NOTI_NTYPE, ntype);
        contentValues.put(SAVE_NOTI_URL, url);
        long rowInserted = db.insert(TABLE_SAVED_NOTIFICATION, null, contentValues);
        db.close();
        return rowInserted != -1;
    }

    public boolean deleteSavedNotificationbyId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SAVED_NOTIFICATION, KEY_ID + "=" + id, null) > 0;
    }

    public boolean deleteSavedNotification(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SAVED_NOTIFICATION, KEY_JID + "=" + id, null) > 0;
    }

    public boolean isSaveNotificationExists(int id) {
        String selectQuery = "";
        selectQuery = "select * from " + TABLE_SAVED_NOTIFICATION + " where " + KEY_JID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        return c.moveToFirst();
    }

    public boolean isSaveNotificationExistsbyId(int id) {
        String selectQuery = "";
        selectQuery = "select * from " + TABLE_SAVED_NOTIFICATION + " where " + KEY_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        return c.moveToFirst();
    }

    public Notifications getValues(int id) {
        String selectQuery = "";
        selectQuery = "select * from " + TABLE_SAVED_NOTIFICATION + " where " + KEY_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        c.moveToFirst();
        Notifications notifications = new Notifications();
        notifications.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        notifications.setTitle(c.getString(c.getColumnIndex(SAVE_NOTI_TITLE)));
        notifications.setMessage(c.getString(c.getColumnIndex(SAVE_NOTI_MESSAGE)));
        notifications.setType(c.getString(c.getColumnIndex(SAVE_NOTI_TYPE)));
        notifications.setDate(c.getString(c.getColumnIndex(SAVE_NOTI_DATE)));
        notifications.setTime(c.getString(c.getColumnIndex(SAVE_NOTI_TIME)));
        notifications.setIsclose(c.getInt(c.getColumnIndex(SAVE_NOTI_IS_CLOSE)));
        notifications.setUrl(c.getString(c.getColumnIndex(SAVE_NOTI_URL)));
        notifications.setBm(c.getString(c.getColumnIndex(SAVE_NOTI_BM)));
        notifications.setNtype(c.getString(c.getColumnIndex(SAVE_NOTI_NTYPE)));
        c.close();
        return notifications;
    }

    public List<Notifications> getSavedNotifications() {
        int vall = 0;
        List<Notifications> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        String query = "select * from " + TABLE_SAVED_NOTIFICATION + " order by " + KEY_ID + " desc";
        c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                int idd;
                String title;
                String message;
                String date;
                String time;
                String isClose;
                String type;
                String bm;
                String ntype;
                String url;

                idd = c.getInt(c.getColumnIndex(KEY_ID));
                title = c.getString(c.getColumnIndex(SAVE_NOTI_TITLE));
                message = c.getString(c.getColumnIndex(SAVE_NOTI_MESSAGE));
                date = c.getString(c.getColumnIndex(SAVE_NOTI_DATE));
                time = c.getString(c.getColumnIndex(SAVE_NOTI_TIME));
                isClose = c.getString(c.getColumnIndex(SAVE_NOTI_IS_CLOSE));
                type = c.getString(c.getColumnIndex(SAVE_NOTI_TYPE));
                bm = c.getString(c.getColumnIndex(SAVE_NOTI_BM));
                ntype = c.getString(c.getColumnIndex(SAVE_NOTI_NTYPE));
                url = c.getString(c.getColumnIndex(SAVE_NOTI_URL));

                Notifications notifications = new Notifications();
                notifications.setId(idd);
                notifications.setTitle(title);
                notifications.setMessage(message);
                notifications.setDate(date);
                notifications.setTime(time);
                notifications.setIsclose(Integer.parseInt(isClose));
                notifications.setType(type);
                notifications.setBm(bm);
                notifications.setNtype(ntype);
                notifications.setUrl(url);

                list.add(notifications);

            } while (c.moveToNext());
        }
        return list;
    }

    public boolean insertReadId(String jobId, String titleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(READ_COLUMN_JOB_ID, jobId);
        values.put(KEY_TITLE_ID, titleId);
        db.insert(READ_TABLE_NAME, null, values);
        return true;
    }

    public boolean deleteReadId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(READ_TABLE_NAME, READ_COLUMN_JOB_ID + "=" + id, null) > 0;
    }

    public boolean isReadTitleExists(int titleid) {

        String selectQuery = "";
        selectQuery = "select * from " + READ_TABLE_NAME + " where " + KEY_TITLE_ID + " = " + titleid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        Boolean a = c.moveToFirst();
        c.close();
        return a;
    }

    public String getReadIds(/*int count*/) {
//        count = 25 * count;
        String selectQuery = "";
        selectQuery = "select DISTINCT * from " + READ_TABLE_NAME + " Order by " + READ_COLUMN_ID + " desc limit 500";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        StringBuilder str = new StringBuilder();
        if (c.getCount() != 0) {
            do {
                str.append(c.getString(c.getColumnIndex(READ_COLUMN_JOB_ID))).append(",");
            } while (c.moveToNext());
            c.close();
        } else {
            str.append("0");
        }
        return str.toString().substring(0, str.length() - 1);
    }

    public boolean addAppliedJob(String jobId, String titleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_JID, jobId);
        contentValues.put(KEY_TITLE_ID, titleId);
        long rowInserted = db.insert(TABLE_APPLIED_JOBS, null, contentValues);
        db.close();
        return rowInserted != -1;
    }

    public boolean isAppliedTitleExists(int titleid) {

        String selectQuery = "";
        selectQuery = "select * from " + TABLE_APPLIED_JOBS + " where " + KEY_TITLE_ID + " = " + titleid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        Boolean a = c.moveToFirst();
        c.close();
        return a;
    }

    public String getAppliedTitleIds() {
        String selectQuery = "";
        selectQuery = "select DISTINCT * from " + TABLE_APPLIED_JOBS + " Order by " + KEY_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        StringBuilder str = new StringBuilder();
        if (c.getCount() != 0) {
            do {
                str.append(c.getString(c.getColumnIndex(KEY_TITLE_ID))).append(",");
            } while (c.moveToNext());
            c.close();
            Log.e("AppReadId", "AppliedId :" + str.toString().substring(0, str.length() - 1));
        } else {
            str.append("0");
            Log.e("AppReadId", "AppliedId :" + str.toString().substring(0, str.length() - 1));
        }
        return str.toString().substring(0, str.length() - 1);
    }

    public String getReadTitleIds() {
        String selectQuery = "";
        selectQuery = "select DISTINCT * from " + READ_TABLE_NAME + " Order by " + READ_COLUMN_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        StringBuilder str = new StringBuilder();
        if (c.getCount() != 0) {
            do {
                str.append(c.getString(c.getColumnIndex(KEY_TITLE_ID))).append(",");
            } while (c.moveToNext());
            c.close();
            Log.e("ReadTitleId", "readId :" + str.toString().substring(0, str.length() - 1));
        } else {
            str.append("0");
            Log.e("ReadTitleId", "readId :" + str.toString().substring(0, str.length() - 1));
        }
        return str.toString().substring(0, str.length() - 1);
    }

    // ---------------------------------------------- Employer Tables ------------------------------

    public boolean addDraftJob(PostDraftJob postJob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ACTION_DATE_TIME, postJob.getActionDate());
        contentValues.put(DRAFT_JOBTITILE_ID, postJob.getJobTitle_Id());
        contentValues.put(DRAFT_JOBTITILE, postJob.getJobTitle());
        contentValues.put(DRAFT_COMPANY_ADDRESS, postJob.getCompany_address());
        contentValues.put(DRAFT_JOBLOCATION, postJob.getJobLocation());
        contentValues.put(DRAFT_CANDIDATE_LOCATION, postJob.getCandidateLocation());
        contentValues.put(DRAFT_MOBILE_NUMBER, postJob.getMobileNumber());
        contentValues.put(DRAFT_EMAIL, postJob.getEmail());
        contentValues.put(DRAFT_MARITALSTATUS, postJob.getMaritalStatus());
        contentValues.put(DRAFT_GENDER, postJob.getGender());
        contentValues.put(DRAFT_WEBSITE, postJob.getWebsite());
        contentValues.put(DRAFT_WORKMODE, postJob.getWorkmode());
        contentValues.put(DRAFT_DESCRIPTION, postJob.getDescription());
        contentValues.put(DRAFT_SKILLS, postJob.getSkills());
        contentValues.put(DRAFT_SINGLE_QUALIFICATION, postJob.getSingle_qualification());
        contentValues.put(DRAFT_GROUP_QUALIFICATION, postJob.getGroup_qualification());
        contentValues.put(DRAFT_VACANCY, postJob.getVacancy());
        contentValues.put(DRAFT_EXPERIENCE, postJob.getExperience());
        contentValues.put(DRAFT_AGELIMIT, postJob.getAgeLimit());
        contentValues.put(DRAFT_SALARY, postJob.getSalary());
        contentValues.put(DRAFT_KEYWORD, postJob.getKeyword());
        contentValues.put(DRAFT_LATLON, postJob.getLatlon());
        contentValues.put(DRAFT_CALLOPTION, postJob.getCallOption());
        contentValues.put(DRAFT_MAILOPTION, postJob.getMailOption());
        contentValues.put(DRAFT_MAIL_WITH_RESUME, postJob.getMailWithResume());
        long rowInserted = db.insert(TABLE_DRAFT_JOBS, null, contentValues);
        db.close();
        return rowInserted != -1;
    }

    public boolean updateDraftJob(PostDraftJob postJob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ACTION_DATE_TIME, postJob.getActionDate());
        contentValues.put(DRAFT_JOBTITILE_ID, postJob.getJobTitle_Id());
        contentValues.put(DRAFT_JOBTITILE, postJob.getJobTitle());
        contentValues.put(DRAFT_COMPANY_ADDRESS, postJob.getCompany_address());
        contentValues.put(DRAFT_JOBLOCATION, postJob.getJobLocation());
        contentValues.put(DRAFT_CANDIDATE_LOCATION, postJob.getCandidateLocation());
        contentValues.put(DRAFT_MOBILE_NUMBER, postJob.getMobileNumber());
        contentValues.put(DRAFT_EMAIL, postJob.getEmail());
        contentValues.put(DRAFT_MARITALSTATUS, postJob.getMaritalStatus());
        contentValues.put(DRAFT_GENDER, postJob.getGender());
        contentValues.put(DRAFT_WEBSITE, postJob.getWebsite());
        contentValues.put(DRAFT_WORKMODE, postJob.getWorkmode());
        contentValues.put(DRAFT_DESCRIPTION, postJob.getDescription());
        contentValues.put(DRAFT_SKILLS, postJob.getSkills());
        contentValues.put(DRAFT_SINGLE_QUALIFICATION, postJob.getSingle_qualification());
        contentValues.put(DRAFT_GROUP_QUALIFICATION, postJob.getGroup_qualification());
        contentValues.put(DRAFT_VACANCY, postJob.getVacancy());
        contentValues.put(DRAFT_EXPERIENCE, postJob.getExperience());
        contentValues.put(DRAFT_AGELIMIT, postJob.getAgeLimit());
        contentValues.put(DRAFT_SALARY, postJob.getSalary());
        contentValues.put(DRAFT_KEYWORD, postJob.getKeyword());
        contentValues.put(DRAFT_LATLON, postJob.getLatlon());
        contentValues.put(DRAFT_CALLOPTION, postJob.getCallOption());
        contentValues.put(DRAFT_MAILOPTION, postJob.getMailOption());
        contentValues.put(DRAFT_MAIL_WITH_RESUME, postJob.getMailWithResume());
        return db.update(TABLE_DRAFT_JOBS, contentValues, KEY_ID + " = " + "'" + postJob.getJobId() + "'", null) > 0;
    }

    public void deleteDraftJob(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRAFT_JOBS, KEY_ID + "=" + id, null);
    }

    public List<PostDraftJob> getDraftJobs() {
        List<PostDraftJob> list = new ArrayList<PostDraftJob>();

        String selectQuery = "";
        selectQuery = "select * from " + TABLE_DRAFT_JOBS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        if (c.moveToFirst()) {
            do {
                PostDraftJob job = new PostDraftJob();
                job.setJobId(String.valueOf(c.getInt(c.getColumnIndex(KEY_ID))));
                job.setJobTitle_Id(c.getString(c.getColumnIndex(DRAFT_JOBTITILE_ID)));
                job.setJobTitle(c.getString(c.getColumnIndex(DRAFT_JOBTITILE)));
                job.setCompany_address(c.getString(c.getColumnIndex(DRAFT_COMPANY_ADDRESS)));
                job.setJobLocation(c.getString(c.getColumnIndex(DRAFT_JOBLOCATION)));
                job.setCandidateLocation(c.getString(c.getColumnIndex(DRAFT_CANDIDATE_LOCATION)));
                job.setMobileNumber(c.getString(c.getColumnIndex(DRAFT_MOBILE_NUMBER)));
                job.setEmail(c.getString(c.getColumnIndex(DRAFT_EMAIL)));
                job.setMaritalStatus(c.getString(c.getColumnIndex(DRAFT_MARITALSTATUS)));
                job.setGender(c.getString(c.getColumnIndex(DRAFT_GENDER)));
                job.setWebsite(c.getString(c.getColumnIndex(DRAFT_WEBSITE)));
                job.setWorkmode(c.getString(c.getColumnIndex(DRAFT_WORKMODE)));
                job.setDescription(c.getString(c.getColumnIndex(DRAFT_DESCRIPTION)));
                job.setSkills(c.getString(c.getColumnIndex(DRAFT_SKILLS)));
                job.setSingle_qualification(c.getString(c.getColumnIndex(DRAFT_SINGLE_QUALIFICATION)));
                job.setGroup_qualification(c.getString(c.getColumnIndex(DRAFT_GROUP_QUALIFICATION)));
                job.setVacancy(c.getString(c.getColumnIndex(DRAFT_VACANCY)));
                job.setExperience(c.getString(c.getColumnIndex(DRAFT_EXPERIENCE)));
                job.setAgeLimit(c.getString(c.getColumnIndex(DRAFT_AGELIMIT)));
                job.setSalary(c.getString(c.getColumnIndex(DRAFT_SALARY)));
                job.setKeyword(c.getString(c.getColumnIndex(DRAFT_KEYWORD)));
                job.setLatlon(c.getString(c.getColumnIndex(DRAFT_LATLON)));
                job.setCallOption(c.getString(c.getColumnIndex(DRAFT_CALLOPTION)));
                job.setMailOption(c.getString(c.getColumnIndex(DRAFT_MAILOPTION)));
                job.setMailWithResume(c.getString(c.getColumnIndex(DRAFT_MAIL_WITH_RESUME)));
                long actionDate = 0;
                actionDate = c.getLong(c.getColumnIndex(KEY_ACTION_DATE_TIME));
                job.setActionDate(actionDate);
                list.add(job);
            } while (c.moveToNext());
        }
        return list;
    }

    public PostDraftJob getDraftJobById(String id) {
        String selectQuery = "select * from " + TABLE_DRAFT_JOBS + " where " + KEY_ID + "=" + id;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);
        U.PQ(selectQuery);
        PostDraftJob job = new PostDraftJob();
        if (c.getCount() != 0 && c.moveToFirst()) {
            job.setJobId(String.valueOf(c.getInt(c.getColumnIndex(KEY_ID))));
            job.setJobTitle_Id(c.getString(c.getColumnIndex(DRAFT_JOBTITILE_ID)));
            job.setJobTitle(c.getString(c.getColumnIndex(DRAFT_JOBTITILE)));
            job.setCompany_address(c.getString(c.getColumnIndex(DRAFT_COMPANY_ADDRESS)));
            job.setJobLocation(c.getString(c.getColumnIndex(DRAFT_JOBLOCATION)));
            job.setCandidateLocation(c.getString(c.getColumnIndex(DRAFT_CANDIDATE_LOCATION)));
            job.setMobileNumber(c.getString(c.getColumnIndex(DRAFT_MOBILE_NUMBER)));
            job.setEmail(c.getString(c.getColumnIndex(DRAFT_EMAIL)));
            job.setMaritalStatus(c.getString(c.getColumnIndex(DRAFT_MARITALSTATUS)));
            job.setGender(c.getString(c.getColumnIndex(DRAFT_GENDER)));
            job.setWebsite(c.getString(c.getColumnIndex(DRAFT_WEBSITE)));
            job.setWorkmode(c.getString(c.getColumnIndex(DRAFT_WORKMODE)));
            job.setDescription(c.getString(c.getColumnIndex(DRAFT_DESCRIPTION)));
            job.setSkills(c.getString(c.getColumnIndex(DRAFT_SKILLS)));
            job.setSingle_qualification(c.getString(c.getColumnIndex(DRAFT_SINGLE_QUALIFICATION)));
            job.setGroup_qualification(c.getString(c.getColumnIndex(DRAFT_GROUP_QUALIFICATION)));
            job.setVacancy(c.getString(c.getColumnIndex(DRAFT_VACANCY)));
            job.setExperience(c.getString(c.getColumnIndex(DRAFT_EXPERIENCE)));
            job.setAgeLimit(c.getString(c.getColumnIndex(DRAFT_AGELIMIT)));
            job.setSalary(c.getString(c.getColumnIndex(DRAFT_SALARY)));
            job.setKeyword(c.getString(c.getColumnIndex(DRAFT_KEYWORD)));
            job.setLatlon(c.getString(c.getColumnIndex(DRAFT_LATLON)));
            job.setCallOption(c.getString(c.getColumnIndex(DRAFT_CALLOPTION)));
            job.setMailOption(c.getString(c.getColumnIndex(DRAFT_MAILOPTION)));
            job.setMailWithResume(c.getString(c.getColumnIndex(DRAFT_MAIL_WITH_RESUME)));
            long actionDate = 0;
            actionDate = c.getLong(c.getColumnIndex(KEY_ACTION_DATE_TIME));
            job.setActionDate(actionDate);
        }
        return job;
    }
}
