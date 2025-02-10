package service.impl;

import org.example.blogpostmanagerbackend.BlogPostManagerBackendApplication;
import org.example.blogpostmanagerbackend.entities.Author;
import org.example.blogpostmanagerbackend.entities.Blog;
import org.example.blogpostmanagerbackend.repository.AuthorTableRepository;
import org.example.blogpostmanagerbackend.repository.BlogTableRepository;
import org.example.blogpostmanagerbackend.service.BlogService;
import org.example.blogpostmanagerbackend.service.impl.BlogServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogPostManagerBackendApplication.class)
@Transactional
public class BlogServiceImplTest {

    @Autowired
    private BlogTableRepository blogTableRepository;

    @Autowired
    private AuthorTableRepository authorTableRepository;

    private BlogService blogService;

    @Before
    public void setUp() throws Exception {
        blogService = new BlogServiceImpl(blogTableRepository, authorTableRepository);
    }

    @Test
    @Sql("/testDataSql/dummyData.sql")
    public void testGetBlodMethod() throws Exception {
        //Given

        //When
        Page<Blog> result = blogService.getBlogsByPagination(0);

        //Then
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());
        assertEquals(12, result.getPageable().getPageSize());
        assertEquals(0, result.getPageable().getPageNumber());

    }

    @Test
    public void testCreateBlogMethod() throws Exception {
        //Given
        Blog blogRequest = createDummyBlog();

        //When
        blogService.createBlog(blogRequest);

        //Then
        Assert.assertEquals(1, blogTableRepository.count());
        Blog blogCreated = blogTableRepository.findAll().get(0);
        assertTwoBlogsEquals(blogRequest, blogCreated);
    }

    @Test
    public void testUpdateBlogMethod() throws Exception {
        //Given
        Blog blogRequest = createDummyBlog();
        blogService.createBlog(blogRequest);
        Assert.assertEquals(1, blogTableRepository.count());
        Blog blogCreated = blogTableRepository.findAll().get(0);

        //When
        blogCreated.setTitle("Updated Title");
        blogCreated.getAuthor().setFirstName("Updated First Name");
        blogService.updateBlog(blogCreated);


        //Then
        Optional<Blog> blogCreatedFound = blogTableRepository.findById(blogCreated.getBlogId());
        assertTrue(blogCreatedFound.isPresent());
        assertTwoBlogsEquals(blogCreatedFound.get(), blogCreated);
    }

    @Test
    public void testDeleteBlogMethod() throws Exception {
        //Given
        Blog blogRequest = createDummyBlog();

        //When
        blogService.createBlog(blogRequest);

        //Then
        Assert.assertEquals(1, blogTableRepository.count());
        Blog blogCreated = blogTableRepository.findAll().get(0);

        //When
        blogService.deleteBlog(blogCreated.getBlogId());

        //Then
        assertFalse(blogTableRepository.findById(blogCreated.getBlogId()).isPresent());
    }


    private void assertTwoBlogsEquals(Blog sourceBlog, Blog targetBlog) {
        assertEquals(sourceBlog.getAuthor(), targetBlog.getAuthor());
        assertEquals(sourceBlog.getTitle(), targetBlog.getTitle());
        assertEquals(sourceBlog.getFullBlogText(), targetBlog.getFullBlogText());
        assertEquals(sourceBlog.getDescription(), targetBlog.getDescription());
        assertEquals(sourceBlog.getAuthor().getFirstName(), targetBlog.getAuthor().getFirstName());
        assertEquals(sourceBlog.getAuthor().getLastName(), targetBlog.getAuthor().getLastName());
    }
    private Blog createDummyBlog() {
        Blog blog = new Blog();
        blog.setTitle("Title");
        blog.setDescription("Description");
        blog.setFullBlogText("Full Text");
        blog.setAuthor(createDummyAuthor());
        return blog;
    }

    private Author createDummyAuthor() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");
        return author;
    }
}
