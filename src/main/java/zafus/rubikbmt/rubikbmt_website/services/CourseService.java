package zafus.rubikbmt.rubikbmt_website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zafus.rubikbmt.rubikbmt_website.entities.Course;
import zafus.rubikbmt.rubikbmt_website.repositories.ICourseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private ICourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }
}