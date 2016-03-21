package database;


public class Treatment {
    int id;
    String startdate;
    String enddate;
    String doctor;
    String patient;
    String text;

    public Treatment(int id, String patient,
                       String doctor, String startdate, String enddate,
                       String text) {
        this.id = id;
        this.startdate = startdate;
        this.enddate = enddate;
        this.doctor = doctor;
        this.patient = patient;
        this.text = text;

    }


    public int getId() {
        return id;
    }
    public String getStartdate() {
        return startdate;
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

    public String getEnddate(){
        return enddate;
    }

    @Override
    public String toString() {
        return "Remember to " + text + " today.";
    }
}

