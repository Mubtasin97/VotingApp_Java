package com.rex.votingapp.controller;

import com.rex.votingapp.entity.Candidate;
import com.rex.votingapp.entity.Voter;
import com.rex.votingapp.entity.Vote;
import com.rex.votingapp.entity.VotingStatus;
import com.rex.votingapp.repository.CandidateRepository;
import com.rex.votingapp.repository.VoteRepository;
import com.rex.votingapp.repository.VoterRepository;
import com.rex.votingapp.repository.VotingStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
public class VotingController {

    @Autowired
    private VotingStatusRepository votingStatusRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private VoterRepository voterRepository;
    @Autowired
    private VoteRepository voteRepository;

    @GetMapping("/")
    public String homepage(Model model) {
        VotingStatus status = votingStatusRepository.findById(1L)
                .orElseGet(() -> {
                    VotingStatus newStatus = new VotingStatus();
                    newStatus.setId(1L);
                    newStatus.setVotingActive(false);
                    return votingStatusRepository.save(newStatus);
                });
        model.addAttribute("votingStatus", status);
        return "homepage";
    }

    @GetMapping("/admin_login")
    public String adminLogin() {
        return "admin_login";
    }

    @GetMapping("/admin_dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        return "admin_dashboard";
    }

    @GetMapping("/edit_candidates")
    public String editCandidates(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        return "edit_candidates";
    }

    @PostMapping("/add_candidate")
    public String addCandidate(@RequestParam String name, RedirectAttributes redirectAttributes) {
        if (name == null || name.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Candidate name cannot be empty");
            return "redirect:/edit_candidates";
        }
        Candidate candidate = new Candidate();
        candidate.setName(name);
        candidateRepository.save(candidate);
        redirectAttributes.addFlashAttribute("message", "Candidate added successfully");
        return "redirect:/edit_candidates";
    }

    @PostMapping("/start_vote")
    public String startVote(RedirectAttributes redirectAttributes) {
        VotingStatus status = votingStatusRepository.findById(1L)
                .orElseGet(() -> {
                    VotingStatus newStatus = new VotingStatus();
                    newStatus.setId(1L);
                    return newStatus;
                });
        if (candidateRepository.count() == 0) {
            redirectAttributes.addFlashAttribute("error", "Cannot start voting without candidates");
            return "redirect:/edit_candidates";
        }
        status.setVotingActive(true);
        votingStatusRepository.save(status);
        redirectAttributes.addFlashAttribute("message", "Voting started successfully");
        return "redirect:/admin_dashboard";
    }

    @GetMapping("/cast_vote")
    public String castVoteRedirect() {
        return "cast_vote";
    }

    @PostMapping("/cast_vote")
    public String castVote(@RequestParam String voterName, HttpSession session, RedirectAttributes redirectAttributes) {
        VotingStatus status = votingStatusRepository.findById(1L).orElseThrow();
        if (!status.isVotingActive()) {
            redirectAttributes.addFlashAttribute("error", "Voting is not active");
            return "redirect:/";
        }
        if (voterName == null || !voterName.matches("[a-zA-Z]+")) {
            redirectAttributes.addFlashAttribute("error", "Voter name must contain only letters");
            return "redirect:/cast_vote";
        }
        if (voterRepository.existsByName(voterName)) {
            redirectAttributes.addFlashAttribute("error", "Voter has already voted");
            return "redirect:/cast_vote";
        }
        session.setAttribute("voterName", voterName);
        return "redirect:/vote_candidate";
    }

    @GetMapping("/vote_candidate")
    public String voteCandidate(Model model) {
        VotingStatus status = votingStatusRepository.findById(1L).orElseThrow();
        if (!status.isVotingActive()) {
            return "redirect:/";
        }
        model.addAttribute("candidates", candidateRepository.findAll());
        return "vote_candidate";
    }

    @PostMapping("/submit_vote")
    public String submitVote(@RequestParam Long candidateId, HttpSession session, RedirectAttributes redirectAttributes) {
        VotingStatus status = votingStatusRepository.findById(1L).orElseThrow();
        if (!status.isVotingActive()) {
            redirectAttributes.addFlashAttribute("error", "Voting is not active");
            return "redirect:/";
        }
        String voterName = (String) session.getAttribute("voterName");
        if (voterName == null) {
            redirectAttributes.addFlashAttribute("error", "No voter session found");
            return "redirect:/cast_vote";
        }
        Voter voter = new Voter();
        voter.setName(voterName);
        voterRepository.save(voter);

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidateRepository.findById(candidateId).orElseThrow());
        vote.setTimestamp(LocalDateTime.now());
        voteRepository.save(vote);

        session.removeAttribute("voterName");
        redirectAttributes.addFlashAttribute("message", "Vote casted successfully");
        return "redirect:/";
    }

    @GetMapping("/total_votes")
    public String totalVotes(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        return "total_votes";
    }

    @PostMapping("/reset_vote")
    public String resetVote(RedirectAttributes redirectAttributes) {
        VotingStatus status = votingStatusRepository.findById(1L).orElseThrow();
        status.setVotingActive(false);
        votingStatusRepository.save(status);
        voteRepository.deleteAll();
        voterRepository.deleteAll();
        candidateRepository.deleteAll();
        redirectAttributes.addFlashAttribute("message", "Voting reset successfully");
        return "redirect:/admin_dashboard";
    }
}