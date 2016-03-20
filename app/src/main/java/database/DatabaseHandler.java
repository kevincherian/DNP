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
    private static final String COLUMN_NAME_APPOINTMENT_ID = "appointment_id"; //online database id of appointment
    private static final String COLUMN_NAME_APPOINTMENT_PATIENT = "patient";
    private static final String COLUMN_NAME_APPOINTMENT_DOCTOR = "doctor";
    private static final String COLUMN_NAME_APPOINTMENT_TIME = "time";
    private static final String COLUMN_NAME_APPOINTMENT_INFO = "info";
    private static final String COLUMN_NAME_APPOINTMENT_STATUS = "status";

    // treatment Table Columns names
    private static final String COLUMN_NAME_TREATMENT_ID = "treament_id"; //online database id of appointment
    private static final String COLUMN_NAME_TREATMENT_PATIENT = "patient";
    private static final String COLUMN_NAME_TREATMENT_DOCTOR = "doctor";
    private static final String COLUMN_NAME_TREATMENT_STARTDATE = "startdate";
    private static final String COLUMN_NAME_TREATMENT_ENDDATE = "enddate";
    private static final String COLUMN_NAME_TREATMENT_TEXT = "text";

    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DATETIME_TYPE = " DATETIME";
    private static final String DATE_TYPE = " DATE";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";


    private static String CREATE_APPOINTMENTS_TABLE =
            "CREATE TABLE " + TABLE_APPOINTMENTS + "("
                    + "id INTEGER PRIMARY KEY,"
                    + COLUMN_NAME_APPOINTMENT_ID + INTEGER_TYPE + COMMA_SEP
                    + COLUMN_NAME_APPOINTMENT_PATIENT + TEXT_TYPE + COMMA_SEP
                    + COLUMN_NAME_APPOINTMENT_DOCTOR + TEXT_TYPE + COMMA_SEP
                    + COLUMN_NAME_APPOINTMENT_TIME + DATETIME_TYPE + COMMA_SEP
                    + COLUMN_NAME_APPOINTMENT_STATUS + TEXT_TYPE + COMMA_SEP
                    + COLUMN_NAME_APPOINTMENT_INFO + TEXT_TYPE +
                    " )";

    private static String DROP_APPOINTMENTS_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS;

    private static String CREATE_TREATMENTS_TABLE =
            "CREATE TABLE " + TABLE_TREATMENT + "("
                    + "id INTEGER PRIMARY KEY,"
                    + COLUMN_NAME_TREATMENT_ID + INTEGER_TYPE + COMMA_SEP
                    + COLUMN_NAME_TREATMENT_PATIENT + TEXT_TYPE + COMMA_SEP
                    + COLUMN_NAME_TREATMENT_DOCTOR + TEXT_TYPE + COMMA_SEP
                    + COLUMN_NAME_TREATMENT_STARTDATE + DATE_TYPE + COMMA_SEP
                    + COLUMN_NAME_TREATMENT_ENDDATE + DATE_TYPE + COMMA_SEP
                    + COLUMN_NAME_TREATMENT_TEXT + TEXT_TYPE +
                    " )";

    private static String DROP_TREATMENTS_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_TREATMENT;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DROP_APPOINTMENTS_TABLE);
        db.execSQL(CREATE_APPOINTMENTS_TABLE);

        db.execSQL(DROP_TREATMENTS_TABLE);
        db.execSQL(CREATE_TREATMENTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
        values.put(COLUMN_NAME_APPOINTMENT_PATIENT, patient);
        values.put(COLUMN_NAME_APPOINTMENT_DOCTOR, doctor);
        values.put(COLUMN_NAME_APPOINTMENT_TIME, time);
        values.put(COLUMN_NAME_APPOINTMENT_STATUS, status);
        values.put(COLUMN_NAME_APPOINTMENT_INFO, info);

        // Inserting Row
        db.insert(TABLE_APPOINTMENTS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new treatment
    public void addTreatment(int id, String patient, String doctor,
                               String startdate, String enddate, String text) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TREATMENT_ID, id);
        values.put(COLUMN_NAME_TREATMENT_PATIENT, patient);
        values.put(COLUMN_NAME_TREATMENT_DOCTOR, doctor);
        values.put(COLUMN_NAME_TREATMENT_STARTDATE, startdate);
        values.put(COLUMN_NAME_TREATMENT_TEXT, text);
        values.put(COLUMN_NAME_TREATMENT_ENDDATE, enddate);

        // Inserting Row
        db.insert(TABLE_TREATMENT, null, values);
        db.close(); // Closing database connection
    }

    // Getting single appointment
    public Appointment getAppointment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_APPOINTMENTS, new String[]{
                        COLUMN_NAME_APPOINTMENT_ID,
                        COLUMN_NAME_APPOINTMENT_PATIENT,
                        COLUMN_NAME_APPOINTMENT_DOCTOR,
                        COLUMN_NAME_APPOINTMENT_TIME,
                        COLUMN_NAME_APPOINTMENT_STATUS,
                        COLUMN_NAME_APPOINTMENT_INFO
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

    // Getting single treatment
    public Treatment getTreatment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TREATMENT, new String[]{
                        COLUMN_NAME_TREATMENT_ID,
                        COLUMN_NAME_TREATMENT_PATIENT,
                        COLUMN_NAME_TREATMENT_DOCTOR,
                        COLUMN_NAME_TREATMENT_STARTDATE,
                        COLUMN_NAME_TREATMENT_ENDDATE,
                        COLUMN_NAME_TREATMENT_TEXT
                }, COLUMN_NAME_TREATMENT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Treatment treatment = new Treatment(
                Integer.parseInt(cursor.getString(1)),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
        return treatment;
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

    // Getting All Appointment on current date
    public List<Appointment> getAllAppointmentsToday() {
        List<Appointment> appointmentList = new ArrayList<Appointment>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_APPOINTMENTS + " WHERE date(time) = date('now')";

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

    // Getting All Treatment
    public List<Treatment> getAllTreatments() {
        List<Treatment> treatmentList = new ArrayList<Treatment>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TREATMENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Treatment treatment = new Treatment(
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
                // Adding contact to list
                treatmentList.add(treatment);
            } while (cursor.moveToNext());
        }

        // return contact list
        return treatmentList;
    }

    // Getting All Treatments today
    public List<Treatment> getAllTreatmentsToday() {
        List<Treatment> treatmentList = new ArrayList<Treatment>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TREATMENT + " WHERE date(startdate) <= date('now')"
                + " AND date(enddate) >= date('now')";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Treatment treatment = new Treatment(
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
                // Adding contact to list
                treatmentList.add(treatment);
            } while (cursor.moveToNext());
        }

        // return contact list
        return treatmentList;
    }

    // Deleting all appointments
    public void deleteAllAppointments() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Create tables again
        db.execSQL(DROP_APPOINTMENTS_TABLE);
        db.execSQL(CREATE_APPOINTMENTS_TABLE);
    }

    // Deleting all treatments
    public void deleteAllTreatments() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Create tables again
        db.execSQL(DROP_TREATMENTS_TABLE);
        db.execSQL(CREATE_TREATMENTS_TABLE);
    }
}
