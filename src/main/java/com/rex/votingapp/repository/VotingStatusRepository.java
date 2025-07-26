package com.rex.votingapp.repository;

import com.rex.votingapp.entity.VotingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingStatusRepository extends JpaRepository<VotingStatus, Long> {
}