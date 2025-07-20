package com.example.courseservice.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Table
@Data
@Entity
public class Sections {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title")//,  nullable = false
    private String title;

    @Column(name = "position")
    private int position;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Courses course;

}
