package com.bmstu.rsoi_lab3.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Александр on 23.02.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class NameMapWrapper {
    private Long id;
    private String name;


    public NameMapWrapper() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
