package com.example.courseservice.controller;

import com.example.courseservice.dto.*;
import com.example.courseservice.mapper.Mapper;
import com.example.courseservice.services.CoursesServices;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


    private final CoursesServices coursesServices;

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
    public ResponseEntity<List<ShowInstructorCourses>> getAllInstructorCourses(@Valid @RequestBody GetAllCoursesWithInstructorId instructorId) {
        // DONE


        return ResponseEntity.status(HttpStatus.OK).body(coursesServices.getAllCoursesWithInstructorId(instructorId));
    }


    /*
        /create-course this method will take as an input text, description, instructorId and category

         communicate with user service to check if the user in user service if user was not found will return error

        if it passes all that will save the course info

    */

    @PostMapping("/create-course")
    @Operation(summary = "this method will create new course", description = """
             this will take some course info to create it like title,description instructor email, category ...\s
             and will generate new course \s
             some errors that may happens or code other than 200
             505, another service is down and could not complete the request \s
             500, something went wrong   \s
            \s""")
    public ResponseEntity<Void> createCourses(@Valid @ModelAttribute CreateCourse course) {

        coursesServices.createCourse(course);


        return ResponseEntity.ok().build();
    }


    /*
    /create-tags this method to add tags to use it in courses

    check if the tag in DB if yes won't save it if not it will save it

    if the tag was in DB after execute  the method it will give back a list with a tags that were in DB and not saved

     */

    @PostMapping("/create-tags")
    @Operation(summary = "this method no longer needed"
            , description = "now what it does just delete/drop courses table this will be no longer here after completing this service ")
    public ResponseEntity<Void> addTags(@Valid @RequestBody List<String> tags) {
        // this method will be removed but for now it only used if you want to delete the courses and sections DB

        coursesServices.createTags(tags);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
    /{id}/update  this method will update the course info

    first checks if the course in DB if now throws CourseNotFound

    then update it
     */

    @PatchMapping("/{id}/update")
    @Operation(summary = "this method will update course info by id", description = "can update every thing except id and instructor email")
    public ResponseEntity<Void> updateCourse(@PathVariable UUID id, @Valid @RequestBody UpdateCourse updateCourse) {
        // DONE

        coursesServices.updateCourse(id, updateCourse);


        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/list-by-categorise")
    @Operation(summary = "this method will return a list of categorise", description = "this method will return every category in DB")
    public ResponseEntity<List<String>> categorise() {
        // DONE

        List<String> list = coursesServices.getCoursesCategorise();

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    /*
    this method to search for courses in a specific category ex: IT
     */

    @GetMapping("/get-courses-by-category/{category}")
    @Operation(summary = "this method will response with 5 courses fom that category"
            , description = """
            this will search for courses has same category as entered \s
            this method is case sensitive so as the same category you want enter it with the same cases
            """)
    public ResponseEntity<List<ShowInstructorCourses>> showCoursesByCategory(@PathVariable String category) {
        // DONE
        List<ShowInstructorCourses> courses = coursesServices.getCoursesByCategory(category).stream()
                .map(Mapper::showCoursesInfo).collect(Collectors.toList());

        if (courses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        } else {

            return ResponseEntity.status(HttpStatus.OK).body(courses);
        }
    }


    /*

        this method will print every tag stored in DB to make it easier to the user to chose from them and if there is a tag
        that exists that he want to use it

     */

    @GetMapping("/show-tags")
    @Operation(summary = "this api will show a list of tags name" , description = """
            this method will return all the tags that exists in DB and show it to user \s
            to chose a list of tags to add to his course
            """)
    public ResponseEntity<List<ShowTagsName>> showTagsName (){
        // DONE

        return ResponseEntity.status(HttpStatus.OK).body(coursesServices.getAllTags());
    }


    /*

    this method will search for a keyword in title or description in DB if it matches
    it will return the courses that matched

     */
    @GetMapping("/search/{searched}")
    @Operation(summary = "this method will search for a courses ", description = """
            this method will search for the courses that contains/has the searched for in description or title \s
            it will return 200 only 500 for bad things happened that i dont know
            """)
    public ResponseEntity<List<ShowInstructorCourses>> searchingForCourses(@PathVariable String searched) {
        //DONE

        List<ShowInstructorCourses> courses = coursesServices.searchForCourse(searched);

        if(courses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        }

        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

}
