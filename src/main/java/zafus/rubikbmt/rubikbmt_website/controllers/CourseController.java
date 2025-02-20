package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.Course;
import zafus.rubikbmt.rubikbmt_website.entities.CubeSubject;
import zafus.rubikbmt.rubikbmt_website.entities.LevelRating;
import zafus.rubikbmt.rubikbmt_website.services.CourseService;
import zafus.rubikbmt.rubikbmt_website.services.CubeSubjectService;
import zafus.rubikbmt.rubikbmt_website.services.LevelRatingService;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CubeSubjectService cubeSubjectService;

    @Autowired
    private LevelRatingService levelRatingService;

    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("cubeSubjects", cubeSubjectService.getAllCubeSubjects());
        model.addAttribute("levelRatings", levelRatingService.getAllLevelRatings());
        return "courses/create";
    }

    @PostMapping("/create")
    public String createCourse(@ModelAttribute Course course) {
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Course course = courseService.getCourseById(id).orElse(null);
        if (course == null) {
            return "redirect:/courses";
        }
        model.addAttribute("course", course);
        model.addAttribute("cubeSubjects", cubeSubjectService.getAllCubeSubjects());
        model.addAttribute("levelRatings", levelRatingService.getAllLevelRatings());
        return "courses/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable String id, @ModelAttribute Course course) {
        course.setId(id);
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}