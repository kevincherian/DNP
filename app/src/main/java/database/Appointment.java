package database;


public class Appointment {
    int id;
    String time;
    String doctor;
    String patient;
    String info;
    String status;

    public Appointment(int id, String patient,
                       String doctor, String time,
                       String status, String info) {
        this.id = id;
        this.time = time;
        this.doctor = doctor;
        this.patient = patient;
        this.status = status;
        this.info = info;

    }


    public int getId() {
        return id;
    }
    public String getTime() {
        return time;
    }
    public String getDoctor() {
        return doctor;
    }
    public String getPatient() {
        return patient;
    }
    public String getInfo() {
        return info;
    }
    public String getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return "Appointment between " + doctor + " and "
                + patient + " at " + time;
    }
}
