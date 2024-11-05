package zafus.rubikbmt.rubikbmt_website.controllers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;
import zafus.rubikbmt.rubikbmt_website.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final CompetitionService competitionService;
    private final CandidateService candidateService;
    @Autowired
    private UserService userService;

    public GlobalControllerAdvice(CompetitionService competitionService, CandidateService candidateService) {
        this.competitionService = competitionService;
        this.candidateService = candidateService;
    }


    public User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            User user = userService.findByUserName(((UserDetails) principal).getUsername());
            return user;
        }
        return null;
    }
    public List<String> getUserRoles() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            // Lấy danh sách quyền (roles) của người dùng
            return userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }
        return null; // Trả về null nếu không tìm thấy người dùng
    }

    @ModelAttribute
    public void addCompetitionsToModel(Model model) {
        List<Competition> competitions = competitionService.getAll();
        User user = getLoggedInUser();
        List<String> roles = getUserRoles();

        model.addAttribute("competitions", competitions);
        List<Object[]> statistics = candidateService.getCountCandidatesByEvent();
        model.addAttribute("statistics", statistics);
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
    }

//    @GetMapping()
//    public void getCandidateStatistics(Model model) {
//        List<Object[]> statistics = candidateService.getCountCandidatesByEvent();
//        model.addAttribute("statistics", statistics);
//    }
}