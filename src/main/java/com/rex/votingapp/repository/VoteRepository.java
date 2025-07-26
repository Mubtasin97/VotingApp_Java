package com.rex.votingapp.repository;

import com.rex.votingapp.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT v.candidate.name AS candidateName, COUNT(v) AS count FROM Vote v GROUP BY v.candidate.name ORDER BY v.candidate.name")
    List<Map<String, Object>> countVotesByCandidate();
}