package com.example.courseservice.services;

import com.example.courseservice.dao.CoursesDao;
import com.example.courseservice.dao.TagsDao;
import com.example.courseservice.dto.CreateCourse;
import com.example.courseservice.dto.GetAllCoursesWithInstructorId;
import com.example.courseservice.dto.UpdateCourse;
import com.example.courseservice.exception.CourseNotFound;
import com.example.courseservice.exception.TagExists;
import com.example.courseservice.exception.UserNotFound;
import com.example.courseservice.grpc.CourseUserGrpcService;
import com.example.courseservice.model.Courses;
import com.example.courseservice.model.Tags;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CoursesServices {
    private final CoursesDao coursesDao;
    private final CourseUserGrpcService courseUserGrpcService;
    private final TagsDao tagsDao;

    public CoursesServices(CoursesDao coursesDao, CourseUserGrpcService courseUserGrpcService, TagsDao tagsDao) {
        this.coursesDao = coursesDao;
        this.courseUserGrpcService = courseUserGrpcService;
        this.tagsDao = tagsDao;
    }


    public List<Courses> getAllCoursesWithInstructorId(GetAllCoursesWithInstructorId instructorId) {


        return coursesDao.findCoursesByInstructorEmail(instructorId.email());
    }


    public void createCourse(CreateCourse course) {

        try {
            if (courseUserGrpcService.checkUserExists(course.instructorEmail()).getUserStatus()) {

                Courses courses = new Courses();
                courses.setInstructorEmail(course.instructorEmail());
                courses.setTitle(course.title());
                courses.setDescription(course.description());
                courses.setCategory(course.category());
                courses.setCreatedAt(new Date(System.currentTimeMillis()));

                List<Tags> tags = new ArrayList<>();

                for (String name : course.tags()) {
                    tags.add(tagsDao.findTagsByName(name));
                }

                courses.setTags(tags);

                coursesDao.save(courses);

            } else {

                throw new UserNotFound("user with this email" + course.instructorEmail() + " was not found");

            }
        } catch (StatusRuntimeException grpcException) {

            throw grpcException;
        }

    }

    public void createTags(List<String> tags) {
        Tags temp;
        List<String> inDB = new ArrayList<>();

        for (String name : tags) {

            temp = new Tags();
            temp.setName(name);
            if (tagsDao.existsTagsByName(name)) {
                inDB.add(name);
                continue;
            }
            tagsDao.save(temp);

        }

        if (!inDB.isEmpty()) {
            throw new TagExists("tag already in DB with this name: " + inDB);
        }

    }

    public void updateCourse(UUID id, UpdateCourse updateCourse) {

        Courses courses = coursesDao.findCoursesById(id);
        if(courses == null) {
            throw new CourseNotFound("course with id: " + id.toString() + " was not found");
        }

        courses.setTitle(updateCourse.title());
        courses.setDescription(updateCourse.description());
        courses.setCategory(updateCourse.category());

        coursesDao.save(courses);
    }


}
