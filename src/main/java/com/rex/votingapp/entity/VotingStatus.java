package com.rex.votingapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class VotingStatus {
    @Id
    private Long id;
    private boolean isVotingActive;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public boolean isVotingActive() { return isVotingActive; }
    public void setVotingActive(boolean isVotingActive) { this.isVotingActive = isVotingActive; }
}