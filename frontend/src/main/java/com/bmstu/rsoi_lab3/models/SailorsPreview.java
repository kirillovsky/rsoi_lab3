package com.bmstu.rsoi_lab3.models;

import com.bmstu.rsoi_lab3.markers.SailorBackend;

/**
 * Created by Александр on 09.02.2016.
 */

public class SailorsPreview implements SailorBackend {

    private Long id;
    private String firstName;
    private String lastName;
    private Long shipEmpl;

    public Long getShipEmpl() {
        return shipEmpl;
    }

    public void setShipEmpl(Long shipEmpl) {
        this.shipEmpl = shipEmpl;
    }


    public SailorsPreview(Sailors sailor) {
        this.id = sailor.getId();
        this.firstName = sailor.getFirstName();
        this.lastName = sailor.getLastName();
        this.shipEmpl = sailor.getShipEmpl();
    }

    public SailorsPreview(Long id, String firstName, String lastName, Long shipEmpl) {
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.shipEmpl = shipEmpl;
    }

    public SailorsPreview() {
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Override
    public String toString() {
        return "SailorsPreview{" +
                "firstName='" + firstName + '\'' +
                ", id=" + id +
                ", lastName='" + lastName + '\'' +
                ", shipEmpl=" + shipEmpl +
                '}';
    }
}
