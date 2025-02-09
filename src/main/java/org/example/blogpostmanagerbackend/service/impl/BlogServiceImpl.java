package org.example.blogpostmanagerbackend.service.impl;

import org.example.blogpostmanagerbackend.entities.Author;
import org.example.blogpostmanagerbackend.entities.Blog;
import org.example.blogpostmanagerbackend.payloads.response.BlogResponse;
import org.example.blogpostmanagerbackend.repository.AuthorTableRepository;
import org.example.blogpostmanagerbackend.repository.BlogTableRepository;
import org.example.blogpostmanagerbackend.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Component
public class BlogServiceImpl implements BlogService {
    private static final int BLOG_PAGE_SIZE = 12;

    private final BlogTableRepository blogTableRepository;
    private final AuthorTableRepository authorTableRepository;

    @Autowired
    public BlogServiceImpl(BlogTableRepository blogTableRepository, AuthorTableRepository authorTableRepository) {
        this.blogTableRepository = blogTableRepository;
        this.authorTableRepository = authorTableRepository;
    }

    @Override
    public BlogResponse createBlog(Blog blogRequest) {
        if (blogTableRepository.findByTitle(blogRequest.getTitle()).isPresent()) {
            return new BlogResponse(HttpStatus.CONFLICT, "Blog title already exists (Not Unique)");
        }

        //Create author if he does not exist
        Author author = blogRequest.getAuthor();
        if (author != null && author.getAuthorId() == null) {
            authorTableRepository.save(author);
        }

        blogTableRepository.save(mapBlog(blogRequest, new Blog()));
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

        authorTableRepository.save(blogRequest.getAuthor());
        blogTableRepository.save(mapBlog(blogRequest, blog.get()));
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

    public Page<Blog> getBlogsByPagination(int page) {
        Pageable pageable = PageRequest.of(page, BLOG_PAGE_SIZE);
        return blogTableRepository.findAll(pageable);
    }

    private Blog mapBlog(Blog sourceBlog, Blog targetBlog) {
        targetBlog.setFullBlogText(sourceBlog.getFullBlogText());
        targetBlog.setDescription(sourceBlog.getDescription());
        targetBlog.setAuthor(sourceBlog.getAuthor());
        targetBlog.setTitle(sourceBlog.getTitle());
        return targetBlog;
    }
}
