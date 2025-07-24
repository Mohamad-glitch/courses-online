package com.example.courseservice.services;

import com.example.courseservice.dao.CoursesDao;
import com.example.courseservice.dao.TagsDao;
import com.example.courseservice.dto.CreateCourse;
import com.example.courseservice.dto.GetAllCoursesWithInstructorId;
import com.example.courseservice.dto.ShowInstructorCourses;
import com.example.courseservice.dto.UpdateCourse;
import com.example.courseservice.exception.CourseNotFound;
import com.example.courseservice.exception.UserNotFound;
import com.example.courseservice.grpc.CourseUserGrpcService;
import com.example.courseservice.mapper.Mapper;
import com.example.courseservice.model.Courses;
import com.example.courseservice.model.Tags;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CoursesServices {
    private static final String PATH = "C:\\Users\\mshlo\\OneDrive\\Desktop\\images\\";
    private final CoursesDao coursesDao;
    private final CourseUserGrpcService courseUserGrpcService;
    private final TagsDao tagsDao;


    public CoursesServices(CoursesDao coursesDao, CourseUserGrpcService courseUserGrpcService, TagsDao tagsDao) {
        this.coursesDao = coursesDao;
        this.courseUserGrpcService = courseUserGrpcService;
        this.tagsDao = tagsDao;
    }


    public List<ShowInstructorCourses> getAllCoursesWithInstructorId(GetAllCoursesWithInstructorId instructorId) {


        return coursesDao.findCoursesByInstructorEmail(instructorId.email()).stream()
                .map(Mapper::showCoursesInfo).collect(Collectors.toList());
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
                courses.setPrice(course.price());

                // image saving process
                File uploadDir = new File(PATH);

                // Generate unique file name with timestamp
                String originalName = course.image().getOriginalFilename();
                String extension = originalName.substring(originalName.lastIndexOf("."));
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = "img_" + timestamp + extension;

                // Save the file
                Path path = Paths.get(PATH + fileName);
                Files.copy(course.image().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                courses.setImage_url(path.toString());


                List<Tags> tags = new ArrayList<>();

                // in this for loop what it does is checks if the tag in DB if yes get it to make the relationship
                // if not save it in DB then added to the courses
                for (String name : course.tags()) {
                    if (tagsDao.existsTagsByName(name)) {
                        tags.add(tagsDao.findTagsByName(name));
                    } else {
                        Tags tag = new Tags();
                        tag.setName(name);
                        tags.add(tagsDao.save(tag));
                    }
                }

                courses.setTags(tags);

                coursesDao.save(courses);

            } else {

                throw new UserNotFound("user with this email" + course.instructorEmail() + " was not found");

            }
        } catch (StatusRuntimeException grpcException) {

            throw grpcException;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void createTags(List<String> tags) {

        List<Courses> allCourses = coursesDao.findAll();

        for (Courses courses : allCourses) {
            coursesDao.delete(courses);
        }

    }

    public void updateCourse(UUID id, UpdateCourse updateCourse) {

        Courses courses = coursesDao.findCoursesById(id);
        if (courses == null) {
            throw new CourseNotFound("course with id: " + id.toString() + " was not found");
        }

        courses.setTitle(updateCourse.title());
        courses.setDescription(updateCourse.description());
        courses.setCategory(updateCourse.category());

        coursesDao.save(courses);
    }


    public List<String> getCoursesCategorise() {

        return coursesDao.getCoursesByRandomCategorise();

    }

    public List<Courses> getCoursesByCategory(String category) {


        return coursesDao.findCoursesByCategory(category);
    }
}
