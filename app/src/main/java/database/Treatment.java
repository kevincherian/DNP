package database;


public class Treatment {
    int id;
    String time;
    String doctor;
    String patient;
    String text;

    public Treatment(int id, String patient,
                       String doctor, String time,
                       String text) {
        this.id = id;
        this.time = time;
        this.doctor = doctor;
        this.patient = patient;
        this.text = text;

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
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Remember to " + text + " at " + time;
    }
}

