package cz3002.dnp.Entity;

import java.util.Date;

/**
 * Created by hizac on 26/2/2016.
 */
public class Appointment {
    int id;
    Date time;
    User doctor;
    User patient;
    String info;
    String status;

    public int getId() {
        return id;
    }
    public Date getTime() {
        return time;
    }
    public User getDoctor() {
        return doctor;
    }
    public User getPatient() {
        return patient;
    }
    public String getInfo() {
        return info;
    }
    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }
    public void setPatient(User patient) {
        this.patient = patient;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
