package com.example.courseservice.controller;

import com.example.courseservice.dao.CoursesDao;
import com.example.courseservice.dto.CreateCourse;
import com.example.courseservice.dto.GetAllCoursesWithInstructorId;
import com.example.courseservice.dto.UpdateCourse;
import com.example.courseservice.model.Courses;
import com.example.courseservice.services.CoursesServices;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CoursesRestController {

    /*
    DB is working fine

    constraints:
        1- tags name are uniq can have the same tag in DB
        2- sections need have course id so the course need to be saved first then adding to it sections
        3- course cased.all for sections for tags has cascade every thing except delete

     */


    private final CoursesServices  coursesServices;

    public CoursesRestController(CoursesServices coursesServices) {
        this.coursesServices = coursesServices;
    }

    /*
    endpoints:

    POST /courses -> create new course

    GET  /courses/{id} -> get the course with id

    GET  /courses/{id}/courses -> show the instructor courses by is id done

    GET  /courses?search=java&page=1 -> searching for courses with tags, title, description

    PUT  /courses/{id}/update -> update the course with this id
     */


    @GetMapping("/instructor/courses")
    @Operation(summary = "gets all instructor courses", description = "this method will get all instructor courses from his id")
    public ResponseEntity<List<Courses>> getAllInstructorCourses(@Valid @RequestBody GetAllCoursesWithInstructorId instructorId) {
        // DONE


        return ResponseEntity.status(HttpStatus.OK).body(coursesServices.getAllCoursesWithInstructorId(instructorId));
    }


    /*
        /create-course this method will take as an input text, description, instructorId and category

         communicate with user service to check if the user in user service if user was not found will return error

        if it passes all that will save the course info

    */

    @PostMapping("/create-course")
    @Operation
    public ResponseEntity<Void> createCourses(@Valid @RequestBody CreateCourse course) {

        coursesServices.createCourse(course);


        return ResponseEntity.ok().build();
    }


    /*
    /create-tags this method to add tags to use it in courses

    check if the tag in DB if yes wont save it if not it will save it

    if the tag was in DB after execute  the method it will give back a list with an tags that were in DB and not saved

     */

    @PostMapping("/create-tags")
    @Operation(summary = "add new tags"
            , description = "add new tags in DB if there was a a tag already saved in DB it will send a message or error message  with unsaved (repeated tags)")
    public ResponseEntity<Void> addTags(@Valid @RequestBody List<String> tags) {
        // DONE

        coursesServices.createTags(tags);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
    /{id}/update  this method will update the course info

    first checks if the course in DB if now throws CourseNotFound

    then update it
     */

    @PatchMapping("/{id}/update")
    public ResponseEntity<Void> updateCourse(@PathVariable UUID id, @Valid @RequestBody UpdateCourse updateCourse) {


        coursesServices.updateCourse(id, updateCourse);


        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
