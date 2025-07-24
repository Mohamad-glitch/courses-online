package com.example.courseservice.mapper;

import com.example.courseservice.dto.ShowInstructorCourses;
import com.example.courseservice.model.Courses;

public class Mapper {

    public static ShowInstructorCourses showCoursesInfo(Courses courses) {
        return  new ShowInstructorCourses(
                courses.getId().toString(),
                courses.getTitle(),
                courses.getDescription(),
                courses.getInstructorEmail(),
                courses.getCategory(),
                String.valueOf(courses.getRating()),
                String.valueOf(courses.getPrice()),
                courses.getImage_url()

        );

    }


}
