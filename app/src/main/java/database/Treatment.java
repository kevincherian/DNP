package database;


public class Treatment {
    int id;
    String time;
    int duration;
    String doctor;
    String patient;
    String text;

    public Treatment(int id, String patient,
                       String doctor, String time, int duration,
                       String text) {
        this.id = id;
        this.time = time;
        this.duration = duration;
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

    public int getDuration(){
        return duration;
    }

    @Override
    public String toString() {
        return "Remember to " + text + " today.";
    }
}

