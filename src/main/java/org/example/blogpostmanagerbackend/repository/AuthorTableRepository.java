package org.example.blogpostmanagerbackend.repository;

import org.example.blogpostmanagerbackend.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorTableRepository extends JpaRepository<Author, Long> {
}
