package org.example.blogpostmanagerbackend.repository;

import org.example.blogpostmanagerbackend.entities.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogTableRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findByTitle(String title);
}
