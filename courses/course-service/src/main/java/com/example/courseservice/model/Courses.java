package com.example.courseservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table
@Entity
@Data
public class Courses {

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title")// , nullable = false
    private String title;

    @Column(name = "description")// ,  nullable = false
    private String description;

    @Column(name = "instructor_email")// ,   nullable = false,  updatable = false,  unique = true
    private String instructorEmail;

    @Column(name = "category")//, nullable = false
    private String category;

    @Column(name = "rating")
    private double rating;

    @Column(name = "rating_count")
    private long ratingCount;

    @Column(name = "enrolled_count")
    private long enrolledCount;

    @Column(name = "total_duration")//,  nullable = false
    private int totalDuration;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")//,  nullable = false
    private Date createdAt;

    @Column(name = "price")
    private double price;

    @Column(name = "course_thumbnails_url",  nullable = false)
    private String image_url;



    @OneToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.REMOVE,
                    CascadeType.DETACH}, mappedBy = "course")// CascadeType.ALL
    private List<Sections> sections;


    @ManyToMany(cascade = {CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "course_tags",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tags> tags;

}
