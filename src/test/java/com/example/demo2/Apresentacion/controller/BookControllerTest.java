package com.example.demo2.Apresentacion.controller;

import com.example.demo2.APresentacion.controller.webModel.BookController;
import com.example.demo2.Domain.model.Page;
import com.example.demo2.Domain.service.BookService;
import com.example.demo2.Domain.service.dto.BookDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import java.math.BigDecimal;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;
    @Test
    @DisplayName("GET /api/books - Should return paginated books")
    void getAllBooksTest() throws Exception {
        // Given
        List<BookDto> bookDtos = List.of(
                new BookDto(1L, "978-1-111-11111-1", "Book One", BigDecimal.valueOf(19.99), BigDecimal.valueOf(5.00),null),
                new BookDto(2L, "978-2-222-22222-2", "Book Two", BigDecimal.valueOf(24.99), BigDecimal.valueOf(10.00),null)
        );
        Page<BookDto> bookPage = new Page<>(bookDtos, 1, 10, 2);

        when(bookService.getAll(1, 10)).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/books")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].isbn").value("978-1-111-11111-1"))
                .andExpect(jsonPath("$.data[1].isbn").value("978-2-222-22222-2"))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(bookService, times(1)).getAll(1, 10);
    }
    @Test
    @DisplayName("GET /api/books/{isbn} - Should return book details")
    void getBookByIsbnTest() throws Exception {
        // Given
        String isbn = "978-1-111-11111-1";
        BookDto bookDto = new BookDto(1L, isbn, "Book One", BigDecimal.valueOf(19.99), BigDecimal.valueOf(5.00), null);
        when(bookService.getByIsbn(isbn)).thenReturn(bookDto);
        // When & Then
        mockMvc.perform(get("/api/books/{isbn}", isbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.titleEs").value("Book One"))
                .andExpect(jsonPath("$.basePrice").value(19.99))
                .andExpect(jsonPath("$.discountPercentage").value(5.00));
        verify(bookService, times(1)).getByIsbn(isbn);
    }
    @Test
    @DisplayName("GET /api/books/{isbn} - Book Not Found")
    void getBookByIsbnNotFoundTest() throws Exception {
        // Given
        String isbn = "978-9-999-99999-9";
        when(bookService.getByIsbn(isbn)).thenThrow(new RuntimeException("Book not found"));
        // When & Then
        mockMvc.perform(get("/api/books/{isbn}", isbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        verify(bookService, times(1)).getByIsbn(isbn);
    }

    @Test
    @DisplayName("GET /api/books - No Books Available")
    void getAllBooksNoBooksTest() throws Exception {
        // Given
        Page<BookDto> emptyPage = new Page<>(List.of(), 1, 10, 0);
        when(bookService.getAll(1, 10)).thenReturn(emptyPage);
        // When & Then
        mockMvc.perform(get("/api/books")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(0));
    }
    @Test
    @DisplayName("Delete /api/books/{isbn} - Should delete book by ISBN")
    void deleteBookByIsbnTest() throws Exception {
        // Given
        String isbn = "978-1-111-11111-1";
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/books/{isbn}", isbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteByIsbn(isbn);
    }
    @Test
    @DisplayName("Delete /api/books/{isbn} - Book Not Found")
    void deleteBookByIsbnNotFoundTest() throws Exception {
        // Given
        String isbn = "978-9-999-99999-9";
        doThrow(new RuntimeException("Book not found")).when(bookService).deleteByIsbn(isbn);
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/books/{isbn}", isbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        verify(bookService, times(1)).deleteByIsbn(isbn);
    }
    @Test
    @DisplayName("Update /api/books/{id} - ID Mismatch")
    void updateBookIdMismatchTest() throws Exception {
        // Given
        Long pathId = 1L;
        Long bodyId = 2L;
        String bookUpdateJson = """
                {
                    "id": %d,
                    "isbn": "978-1-111-11111-1",
                    "titleEs": "Updated Book",
                    "basePrice": 29.99,
                    "discountPercentage": 15.00 
                    }""".formatted(bodyId);
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/books/{id}", pathId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookUpdateJson))
                .andExpect(status().isBadRequest());
        verify(bookService, times(0)).update(any(BookDto.class));
    }
    @Test
    @DisplayName("Update /api/books/{id} - Should update book")
    void updateBookTest() throws Exception {
        // Given
        Long bookId = 1L;
        String bookUpdateJson = """
            {
                "id": %d,
                "isbn": "9781111111111",
                "titleEs": "Updated Book",
                "basePrice": 29.99,
                "discountPercentage": 15.00
                }""".formatted(bookId);
        BookDto updatedBookDto = new BookDto(bookId, "9781111111111", "Updated Book", BigDecimal.valueOf(29.99), BigDecimal.valueOf(15.00), null);
        when(bookService.update(any(BookDto.class))).thenReturn(updatedBookDto);
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.titleEs").value("Updated Book"))
                .andExpect(jsonPath("$.basePrice").value(29.99))
                .andExpect(jsonPath("$.discountPercentage").value(15.00));
        verify(bookService, times(1)).update(any(BookDto.class));
    }
    @Test
    @DisplayName("Create /api/books - Should create new book")
    void createBookTest() throws Exception {
        // Given
        String bookInsertJson = """
                {
                    "isbn": "9781111111111",
                    "titleEs": "New Book",
                    "basePrice": 19.99,
                    "discountPercentage": 5.00
                    }""";
        BookDto createdBookDto = new BookDto(1L, "9781111111111", "New Book", BigDecimal.valueOf(19.99), BigDecimal.valueOf(5.00), null);
        when(bookService.create(any(BookDto.class))).thenReturn(createdBookDto);
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookInsertJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titleEs").value("New Book"))
                .andExpect(jsonPath("$.basePrice").value(19.99))
                .andExpect(jsonPath("$.discountPercentage").value(5.00));
        verify(bookService, times(1)).create(any(BookDto.class));
    }
    @Test
    @DisplayName("Create /api/books - Invalid Data")
    void createBookInvalidDataTest() throws Exception {
        // Given
        String bookInsertJson = """
                {
                    "isbn": "",
                    "titleEs": "New Book",
                    "basePrice": null,
                    "discountPercentage": 5.00
                    }""";
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookInsertJson))
                .andExpect(status().isBadRequest());
        verify(bookService, times(0)).create(any(BookDto.class));
    }



}
