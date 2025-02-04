package org.example.blogpostmanagerbackend.service.impl;

import org.example.blogpostmanagerbackend.entities.Blog;
import org.example.blogpostmanagerbackend.payloads.response.BlogResponse;
import org.example.blogpostmanagerbackend.repository.BlogTableRepository;
import org.example.blogpostmanagerbackend.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Component
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogTableRepository blogTableRepository;

    @Override
    public BlogResponse createBlog(Blog blogRequest) {
        if (blogTableRepository.findByTitle(blogRequest.getTitle()).isPresent()) {
            return new BlogResponse(HttpStatus.CONFLICT, "Blog title already exists (Not Unique)");
        }

        Blog blog = new Blog();
        blog.setTitle(blogRequest.getTitle());
        blog.setDescription(blogRequest.getDescription());
        blog.setAuthor(blogRequest.getAuthor());
        blog.setFullBlogText(blogRequest.getFullBlogText());
        blogTableRepository.save(blog);
        return new BlogResponse(HttpStatus.CREATED, "New Blog Created Successfully");
    }

    @Override
    public List<Blog> getAllBlogs() {
        return blogTableRepository.findAll();
    }

    @Override
    public BlogResponse updateBlog(Blog blogRequest) {
        Optional<Blog> blog = blogTableRepository.findById(blogRequest.getBlogId());
        if (blog.isEmpty()) {
            return new BlogResponse(HttpStatus.NOT_FOUND, "Blog with id " + blogRequest.getBlogId() + " was not found to be updated.");
        }
        Blog blogToUpdate = blog.get();
        blogToUpdate.setFullBlogText(blogRequest.getFullBlogText());
        blogToUpdate.setDescription(blogRequest.getDescription());
        blogToUpdate.setAuthor(blogRequest.getAuthor());
        blogToUpdate.setTitle(blogRequest.getTitle());
        blogTableRepository.save(blogToUpdate);
        return new BlogResponse(HttpStatus.OK, "New Blog Updated Successfully");
    }

    @Override
    public BlogResponse deleteBlog(Long blogId) {
        Optional<Blog> blogToDelete = blogTableRepository.findById(blogId);
        if (blogToDelete.isPresent()) {
            blogTableRepository.deleteById(blogId);
            return new BlogResponse(HttpStatus.OK, "Blog Deleted Successfully");
        }
        return new BlogResponse(HttpStatus.NOT_FOUND, "Blog with id " + blogId + " was not found in database.");
    }
}
