package com.example.demo2.integration;

import com.example.demo2.APresentacion.controller.webModel.request.BookInsertRequest;
import com.example.demo2.APresentacion.controller.webModel.request.BookUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@Transactional
public class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Integration: GET /api/books - Should return paginated books")
    void getAllBooksIntegrationTest() throws Exception {
        mockMvc.perform(get("/api/books")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    @DisplayName("Integration: POST /api/books - Should create new book")
    void createBookIntegrationTest() throws Exception {
        BookInsertRequest newBook = new BookInsertRequest(
                "9999999999999",
                "Integration Test Book",
                BigDecimal.valueOf(39.99),
                BigDecimal.valueOf(15.00)
        );

        String bookJson = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.isbn").value("9999999999999"))
                .andExpect(jsonPath("$.titleEs").value("Integration Test Book"))
                .andExpect(jsonPath("$.basePrice").value(39.99))
                .andExpect(jsonPath("$.discountPercentage").value(15.00))
                .andExpect(jsonPath("$.price").value(33.99));
    }

    @Test
    @DisplayName("Integration: GET /api/books/{isbn} - Should return book by ISBN")
    void getBookByIsbnIntegrationTest() throws Exception {
        // Primero crear un libro
        BookInsertRequest newBook = new BookInsertRequest(
                "8888888888888",
                "Find Me Book",
                BigDecimal.valueOf(29.99),
                BigDecimal.valueOf(10.00)
        );

        String bookJson = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated());

        // Luego buscarlo por ISBN
        mockMvc.perform(get("/api/books/{isbn}", "8888888888888")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("8888888888888"))
                .andExpect(jsonPath("$.titleEs").value("Find Me Book"))
                .andExpect(jsonPath("$.basePrice").value(29.99))
                .andExpect(jsonPath("$.discountPercentage").value(10.00));
    }

    @Test
    @DisplayName("Integration: PUT /api/books/{id} - Should update existing book")
    void updateBookIntegrationTest() throws Exception {
        // Crear libro
        BookInsertRequest newBook = new BookInsertRequest(
                "7777777777777",
                "Original Title",
                BigDecimal.valueOf(49.99),
                BigDecimal.valueOf(5.00)
        );

        String createJson = objectMapper.writeValueAsString(newBook);

        String createResponse = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bookId = objectMapper.readTree(createResponse).get("id").asLong();

        // Actualizar libro
        BookUpdateRequest updateBook = new BookUpdateRequest(
                bookId,
                "7777777777777",
                "Updated Title",
                BigDecimal.valueOf(44.99),
                BigDecimal.valueOf(20.00)
        );

        String updateJson = objectMapper.writeValueAsString(updateBook);

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.titleEs").value("Updated Title"))
                .andExpect(jsonPath("$.basePrice").value(44.99))
                .andExpect(jsonPath("$.discountPercentage").value(20.00));
    }

    @Test
    @DisplayName("Integration: DELETE /api/books/{isbn} - Should delete book by ISBN")
    void deleteBookIntegrationTest() throws Exception {
        // Crear libro
        BookInsertRequest newBook = new BookInsertRequest(
                "6666666666666",
                "Book To Delete",
                BigDecimal.valueOf(19.99),
                BigDecimal.valueOf(0.00)
        );

        String bookJson = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated());

        // Eliminar libro
        mockMvc.perform(delete("/api/books/{isbn}", "6666666666666")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verificar que no existe
        mockMvc.perform(get("/api/books/{isbn}", "6666666666666")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Integration: POST /api/books - Should fail with duplicate ISBN")
    void createBookDuplicateIsbnIntegrationTest() throws Exception {
        BookInsertRequest newBook = new BookInsertRequest(
                "5555555555555",
                "Duplicate Book",
                BigDecimal.valueOf(29.99),
                BigDecimal.valueOf(5.00)
        );

        String bookJson = objectMapper.writeValueAsString(newBook);

        // Crear primer libro
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated());

        // Intentar crear libro duplicado
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BusinessException"))
                .andExpect(jsonPath("$.message").value("Book with isbn 5555555555555 already exists"));
    }

    @Test
    @DisplayName("Integration: POST /api/books - Should fail with invalid data")
    void createBookInvalidDataIntegrationTest() throws Exception {
        BookInsertRequest invalidBook = new BookInsertRequest(
                "", // ISBN vacío
                "Invalid Book",
                null, // precio nulo
                BigDecimal.valueOf(5.00)
        );

        String bookJson = objectMapper.writeValueAsString(invalidBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Integration: GET /api/books/{isbn} - Should return 404 for non-existing book")
    void getBookByIsbnNotFoundIntegrationTest() throws Exception {
        mockMvc.perform(get("/api/books/{isbn}", "0000000000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("ResourceNotFoundException"));
    }

    @Test
    @DisplayName("Integration: PUT /api/books/{id} - Should fail with ID mismatch")
    void updateBookIdMismatchIntegrationTest() throws Exception {
        BookUpdateRequest updateBook = new BookUpdateRequest(
                999L,
                "1234567890123",
                "Test Book",
                BigDecimal.valueOf(29.99),
                BigDecimal.valueOf(10.00)
        );

        String bookJson = objectMapper.writeValueAsString(updateBook);

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("IllegalArgumentException"))
                .andExpect(jsonPath("$.message").value("ID in path and request body must match"));
    }

    @Test
    @DisplayName("Integration: Full CRUD flow for a book")
    void fullCrudFlowIntegrationTest() throws Exception {
        // 1. Crear libro
        BookInsertRequest newBook = new BookInsertRequest(
                "4444444444444",
                "CRUD Test Book",
                BigDecimal.valueOf(59.99),
                BigDecimal.valueOf(25.00)
        );

        String createJson = objectMapper.writeValueAsString(newBook);

        String createResponse = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(44.99))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bookId = objectMapper.readTree(createResponse).get("id").asLong();

        // 2. Leer libro
        mockMvc.perform(get("/api/books/{isbn}", "4444444444444"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titleEs").value("CRUD Test Book"));

        // 3. Actualizar libro
        BookUpdateRequest updateBook = new BookUpdateRequest(
                bookId,
                "4444444444444",
                "CRUD Updated Book",
                BigDecimal.valueOf(49.99),
                BigDecimal.valueOf(10.00)
        );

        String updateJson = objectMapper.writeValueAsString(updateBook);

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titleEs").value("CRUD Updated Book"))
                .andExpect(jsonPath("$.price").value(44.99));

        // 4. Eliminar libro
        mockMvc.perform(delete("/api/books/{isbn}", "4444444444444"))
                .andExpect(status().isNoContent());

        // 5. Verificar eliminación
        mockMvc.perform(get("/api/books/{isbn}", "4444444444444"))
                .andExpect(status().isNotFound());
    }
}
