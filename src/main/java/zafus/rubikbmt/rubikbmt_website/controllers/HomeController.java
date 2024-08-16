package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")

public class HomeController {

    @GetMapping("/cube")
    public String cube(){
        return "home/cube";
    }
    @GetMapping
    public String index(HttpServletRequest request) {
        Boolean isBackToSchool = (Boolean) request.getAttribute("isBackToSchool");
        if (Boolean.TRUE.equals(isBackToSchool)) {
            return "backToSchool/index"; // Trả về view cho subdomain
        } else {
            return "home/index"; // Trả về view cho domain chính
        }
    }

}
