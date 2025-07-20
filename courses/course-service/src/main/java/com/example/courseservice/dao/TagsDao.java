package com.example.courseservice.dao;

import com.example.courseservice.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TagsDao extends JpaRepository<Tags, UUID> {
    boolean existsTagsByName(String tag);

    Tags findTagsByName(String name);


}

