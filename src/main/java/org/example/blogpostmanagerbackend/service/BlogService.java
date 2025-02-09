package org.example.blogpostmanagerbackend.service;

import org.example.blogpostmanagerbackend.entities.Blog;
import org.example.blogpostmanagerbackend.payloads.response.BlogResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlogService {

    BlogResponse createBlog(Blog blogRequest);

    //Aware that it is unused
    List<Blog> getAllBlogs();

    BlogResponse updateBlog(Blog blogRequest);

    BlogResponse deleteBlog(Long blogId);

    Page<Blog> getBlogsByPagination(int page);
}
