package cz3002.dnp.Entity;

import java.util.Date;

/**
 * Created by hizac on 26/2/2016.
 */
public class Communication {
    int id;
    Date date;
    User doctor;
    User patient;
    String message;

    public int getId() {
        return id;
    }
    public Date getDate() {
        return date;
    }
    public User getDoctor() {
        return doctor;
    }
    public User getPatient() {
        return patient;
    }
    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }
    public void setPatient(User patient) {
        this.patient = patient;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
