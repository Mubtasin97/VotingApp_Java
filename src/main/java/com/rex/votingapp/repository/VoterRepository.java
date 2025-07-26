package com.rex.votingapp.repository;

import com.rex.votingapp.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoterRepository extends JpaRepository<Voter, String> {
    boolean existsByName(String voterName);
}