package org.example.blogpostmanagerbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.blogpostmanagerbackend.entities.Blog;
import org.example.blogpostmanagerbackend.payloads.response.BlogResponse;
import org.example.blogpostmanagerbackend.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class BlogController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/getBlogsByPagination/{page}")
    public ResponseEntity<?> getBlogsByPagination(@PathVariable("page") int page) {
        try {
            Page<Blog> blogs = blogService.getBlogsByPagination(page);
            return new ResponseEntity<>(blogs, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in getting all blogs", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createBlog")
    public ResponseEntity<String> createBlog(@RequestBody Blog blogRequest) {
        try {
            BlogResponse blogResponse = blogService.createBlog(blogRequest);
            return new ResponseEntity<>(blogResponse.getMessage(), blogResponse.getHttpStatus());
        } catch (Exception e) {
            log.error("Error in creating blog", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateBlog")
    public ResponseEntity<String> updateBlog(@RequestBody Blog blogRequest) {
        try {
            BlogResponse blogResponse = blogService.updateBlog(blogRequest);
            return new ResponseEntity<>(blogResponse.getMessage(), blogResponse.getHttpStatus());
        } catch (Exception e) {
            log.error("Error in creating blog", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBlog/{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable("blogId") Long blogId) {
        try {
            BlogResponse blogResponse = blogService.deleteBlog(blogId);
            return new ResponseEntity<>(blogResponse.getMessage(), blogResponse.getHttpStatus());
        } catch (Exception e) {
            log.error("Error in creating blog", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
