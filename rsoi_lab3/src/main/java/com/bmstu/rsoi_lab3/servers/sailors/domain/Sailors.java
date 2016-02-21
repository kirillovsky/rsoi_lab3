package com.bmstu.rsoi_lab3.servers.sailors.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Александр on 08.02.2016.
 */

@Entity
public class Sailors implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String speciality;

    @Column(nullable = false, columnDefinition="DATETIME")
    private Date hiredate;

    @Column(nullable = false)
    private Long shipEmpl;

    public Sailors(String firstName, String lastName, String speciality, Date hiredate, Long ship_empl) {
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

    public void setId(Long id) {
        this.id = id;
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
