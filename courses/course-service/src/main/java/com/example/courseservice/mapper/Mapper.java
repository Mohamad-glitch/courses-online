package com.example.courseservice.mapper;

import com.example.courseservice.dto.ShowInstructorCourses;
import com.example.courseservice.dto.ShowTagsName;
import com.example.courseservice.model.Courses;
import com.example.courseservice.model.Tags;

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


    public static ShowTagsName toShowTagsName(Tags tags) {

        StringBuilder name = new StringBuilder();
        name.append(tags.getName().toUpperCase().charAt(0));
        name.append(tags.getName().substring(1).toLowerCase());

        return new ShowTagsName(name.toString());
    }

}
