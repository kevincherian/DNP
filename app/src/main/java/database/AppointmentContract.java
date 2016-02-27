package database;

import android.provider.BaseColumns;


public final class AppointmentContract {
    public AppointmentContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class AppointmentEntry implements BaseColumns {
        public static final String TABLE_NAME = "appointment";
        public static final String COLUMN_NAME_APPOINTMENT_ID = "appointment_id"; //online database id of appointment
        public static final String COLUMN_NAME_PATIENT_ID = "user_id";
        public static final String COLUMN_NAME_DOCTOR_ID = "doctor_id";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_INFO = "info";
        public static final String COLUMN_NAME_STATUS = "status";



        private static final String INTEGER_TYPE = " INTEGER";
        private static final String DATETIME_TYPE = " DATETIME";
        private static final String VARCHAR_TYPE = " VARCHAR";
        private static final String COMMA_SEP = ",";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + AppointmentEntry.TABLE_NAME;

        public static final String SQL_CREATE_ENTRIES =
                SQL_DELETE_ENTRIES + ";" +
                        "CREATE TABLE " + AppointmentEntry.TABLE_NAME + " (" +
                        AppointmentEntry._ID + " INTEGER PRIMARY KEY," +
                        AppointmentEntry.COLUMN_NAME_APPOINTMENT_ID + INTEGER_TYPE + COMMA_SEP +
                        AppointmentEntry.COLUMN_NAME_PATIENT_ID + INTEGER_TYPE + COMMA_SEP +
                        AppointmentEntry.COLUMN_NAME_DOCTOR_ID + INTEGER_TYPE + COMMA_SEP +
                        AppointmentEntry.COLUMN_NAME_TIME + DATETIME_TYPE + COMMA_SEP +
                        AppointmentEntry.COLUMN_NAME_STATUS + VARCHAR_TYPE + COMMA_SEP +
                        AppointmentEntry.COLUMN_NAME_INFO + VARCHAR_TYPE + COMMA_SEP +
                        " )";
    }
}

