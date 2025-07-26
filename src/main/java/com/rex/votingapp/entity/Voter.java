package com.rex.votingapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Voter {
    @Id
    private String name;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}