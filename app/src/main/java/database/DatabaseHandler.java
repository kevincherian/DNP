package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper{


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "database";

    private static final String TABLE_APPOINTMENTS = "appointments";
    private static final String TABLE_TREATMENT = "treatments";


    // appointment Table Columns names
    public static final String COLUMN_NAME_APPOINTMENT_ID = "appointment_id"; //online database id of appointment
    public static final String COLUMN_NAME_PATIENT = "patient";
    public static final String COLUMN_NAME_DOCTOR = "doctor";
    public static final String COLUMN_NAME_TIME = "time";
    public static final String COLUMN_NAME_INFO = "info";
    public static final String COLUMN_NAME_STATUS = "status";

    // treatment Table Columns names

    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DATETIME_TYPE = " DATETIME";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";


    private static String CREATE_APPOINTMENTS_TABLE =
            "CREATE TABLE " + TABLE_APPOINTMENTS + "("
                    + "id INTEGER PRIMARY KEY,"
                    + COLUMN_NAME_APPOINTMENT_ID + INTEGER_TYPE + COMMA_SEP
                    + COLUMN_NAME_PATIENT + TEXT_TYPE + COMMA_SEP
                    + COLUMN_NAME_DOCTOR + TEXT_TYPE + COMMA_SEP
                    + COLUMN_NAME_TIME + DATETIME_TYPE + COMMA_SEP
                    + COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP
                    + COLUMN_NAME_INFO + TEXT_TYPE +
                    " )";

    private static String DROP_APPOINTMENTS_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DROP_APPOINTMENTS_TABLE);

        db.execSQL(CREATE_APPOINTMENTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(DROP_APPOINTMENTS_TABLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new appointment
    public void addAppointment(int id, String patient, String doctor,
                        String time, String status, String info) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_APPOINTMENT_ID, id);
        values.put(COLUMN_NAME_PATIENT, patient);
        values.put(COLUMN_NAME_DOCTOR, doctor);
        values.put(COLUMN_NAME_TIME, time);
        values.put(COLUMN_NAME_STATUS, status);
        values.put(COLUMN_NAME_INFO, info);

        // Inserting Row
        db.insert(TABLE_APPOINTMENTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single appointment
    public Appointment getAppointment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_APPOINTMENTS, new String[]{
                        COLUMN_NAME_APPOINTMENT_ID,
                        COLUMN_NAME_PATIENT,
                        COLUMN_NAME_DOCTOR,
                        COLUMN_NAME_TIME,
                        COLUMN_NAME_STATUS,
                        COLUMN_NAME_INFO
                }, COLUMN_NAME_APPOINTMENT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Appointment ap = new Appointment(
                Integer.parseInt(cursor.getString(1)),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
        return ap;
    }

    // Getting All Appointment
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointmentList = new ArrayList<Appointment>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_APPOINTMENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Appointment ap = new Appointment(
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
                // Adding contact to list
                appointmentList.add(ap);
            } while (cursor.moveToNext());
        }

        // return contact list
        return appointmentList;
    }

    // Deleting single contact
    public void deleteAllAppointments() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DROP_APPOINTMENTS_TABLE);
        // Create tables again
        onCreate(db);
    }

}
