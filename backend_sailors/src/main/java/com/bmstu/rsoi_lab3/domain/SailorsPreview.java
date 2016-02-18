package com.bmstu.rsoi_lab3.domain;

/**
 * Created by Александр on 09.02.2016.
 */

public class SailorsPreview {

    private Long id;
    private String firstName;
    private String lastName;

    public Long getShipEmpl() {
        return shipEmpl;
    }

    private Long shipEmpl;

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

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
