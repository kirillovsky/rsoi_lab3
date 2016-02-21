package com.bmstu.rsoi_lab3.models;

import com.bmstu.rsoi_lab3.markers.SailorBackend;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Александр on 08.02.2016.
 */

public class Sailors implements Serializable, SailorBackend {

    private Long id;

    @NotEmpty(message = "First Name is required.")
    private String firstName;

    @NotEmpty(message = "Last Name is required.")
    private String lastName;

    @NotEmpty(message = "Speciality is required.")
    private String speciality;

    private Date hiredate;

    @NotNull(message = "Ship ID is required.")
    private Long shipEmpl;

    public Sailors(Long id, String firstName, String lastName, String speciality, Date hiredate, Long ship_empl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
        this.hiredate = hiredate;
        this.shipEmpl = ship_empl;
    }

    public Sailors() {
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public Long getShipEmpl() {
        return shipEmpl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setShipEmpl(Long shipEmpl) {
        this.shipEmpl = shipEmpl;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return "Sailors{" +
                "firstName='" + firstName + '\'' +
                ", id=" + id +
                ", lastName='" + lastName + '\'' +
                ", speciality='" + speciality + '\'' +
                ", hiredate=" + hiredate +
                ", shipEmpl=" + shipEmpl +
                '}';
    }
}
