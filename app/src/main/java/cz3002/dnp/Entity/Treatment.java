package cz3002.dnp.Entity;

import java.util.Date;

/**
 * Created by hizac on 26/2/2016.
 */
public class Treatment {
    int id;
    Date startdate;
    Date enddate;
    User doctor;
    User patient;
    String info;

    public int getId() {
        return id;
    }
    public Date getStartdate() {
        return startdate;
    }
    public Date getEnddate() {
        return enddate;
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

    public void setId(int id) {
        this.id = id;
    }
    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
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

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
