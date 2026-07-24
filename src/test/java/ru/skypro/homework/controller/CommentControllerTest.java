package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentEntityService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentEntityService commentEntityService;

    @Test
    void getComments_ShouldReturnComments() throws Exception {
        Comment comment1 = new Comment();
        comment1.setAuthor(1);
        comment1.setAuthorFirstName("Иван");
        comment1.setAuthorImage("/images/avatar1.jpg");
        comment1.setCreatedAt(System.currentTimeMillis());
        comment1.setPk(1);
        comment1.setText("Отличное объявление!");

        Comment comment2 = new Comment();
        comment2.setAuthor(2);
        comment2.setAuthorFirstName("Петр");
        comment2.setAuthorImage("/images/avatar2.jpg");
        comment2.setCreatedAt(System.currentTimeMillis());
        comment2.setPk(2);
        comment2.setText("Интересно");

        Comments comments = new Comments();
        comments.setCount(2);
        comments.setResults(List.of(comment1, comment2));

        when(commentEntityService.getComments(1)).thenReturn(comments);

        mockMvc.perform(get("/ads/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results.length()").value(2))
                .andExpect(jsonPath("$.results[0].text").value("Отличное объявление!"))
                .andExpect(jsonPath("$.results[1].text").value("Интересно"));
    }

    @Test
    @WithMockUser(username = "user")
    void addComment_ShouldReturnCreatedComment() throws Exception {
        CreateOrUpdateComment createComment = new CreateOrUpdateComment();
        createComment.setText("Новый комментарий");

        Comment comment = new Comment();
        comment.setAuthor(1);
        comment.setAuthorFirstName("Иван");
        comment.setAuthorImage("/images/avatar.jpg");
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setPk(3);
        comment.setText("Новый комментарий");

        when(commentEntityService.addComment(anyInt(), any(), any())).thenReturn(comment);

        mockMvc.perform(post("/ads/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Новый комментарий"));
    }
}
