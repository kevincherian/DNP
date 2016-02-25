package cz3002.dnp.Controller;

import java.util.Date;

import cz3002.dnp.Entity.Appointment;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 26/2/2016.
 */
public class AppointmentCtrl {
    // Quickly set info for an appointment object
    public Appointment setAppointmentInfo(Appointment appointment, int id, Date time, User doctor, User patient, String info, String status) {
        appointment.setId(id);
        appointment.setTime(time);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setInfo(info);
        appointment.setStatus(status);

        return appointment;
    }
}
