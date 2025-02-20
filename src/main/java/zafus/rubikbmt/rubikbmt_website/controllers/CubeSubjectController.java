package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.CubeSubject;
import zafus.rubikbmt.rubikbmt_website.services.CubeSubjectService;

import java.util.List;

@Controller
@RequestMapping("/cube-subjects")
public class CubeSubjectController {
    @Autowired
    private CubeSubjectService cubeSubjectService;

    @GetMapping
    public String listCubeSubjects(Model model) {
        List<CubeSubject> cubeSubjects = cubeSubjectService.getAllCubeSubjects();
        model.addAttribute("cubeSubjects", cubeSubjects);
        return "cube_subjects/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("cubeSubject", new CubeSubject());
        return "cube_subjects/create";
    }

    @PostMapping("/create")
    public String createCubeSubject(@ModelAttribute CubeSubject cubeSubject) {
        cubeSubjectService.saveCubeSubject(cubeSubject);
        return "redirect:/cube-subjects";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        CubeSubject cubeSubject = cubeSubjectService.getCubeSubjectById(id).orElse(null);
        if (cubeSubject == null) {
            return "redirect:/cube-subjects";
        }
        model.addAttribute("cubeSubject", cubeSubject);
        return "cube_subjects/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCubeSubject(@PathVariable String id, @ModelAttribute CubeSubject cubeSubject) {
        cubeSubject.setId(id);
        cubeSubjectService.saveCubeSubject(cubeSubject);
        return "redirect:/cube-subjects";
    }

    @GetMapping("/delete/{id}")
    public String deleteCubeSubject(@PathVariable String id) {
        cubeSubjectService.deleteCubeSubject(id);
        return "redirect:/cube-subjects";
    }
}