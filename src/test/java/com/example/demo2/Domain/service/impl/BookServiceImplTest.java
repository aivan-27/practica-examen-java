package com.example.demo2.Domain.service.impl;

import com.example.demo2.Domain.model.Page;
import com.example.demo2.Domain.repository.BookRepository;
import com.example.demo2.Domain.repository.entity.BookEntity;
import com.example.demo2.Domain.service.dto.BookDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;


    @Test
    @DisplayName("Given valid arguments")
    void testGetAll_InvalidPageAndSize_ThrowsIllegalArgumentException() {
        List<BookEntity> books = List.of(
                new BookEntity(1L, "1234567890", "Title 1", new BigDecimal("0.00"), new BigDecimal("0.00")),
                new BookEntity(2L, "0987654321", "Title 2", new BigDecimal("0.00"), new BigDecimal("0.00"))
        );
        List<BookDto> expectedDtos = List.of(
                new BookDto(1L, "1234567890", "Title 1", new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00")),
                new BookDto(2L, "0987654321", "Title 2", new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"))
        );
        when(bookRepository.findAll(1, 10)).thenReturn(new Page<>(books, 1, 10, books.size()));
        Page<BookDto> expected = new Page<>(expectedDtos, 1, 10, books.size());
        Page<BookDto> result = bookServiceImpl.getAll(1, 10);
        assertEquals(expected, result);


    }
    @Test
    @DisplayName("Getall Given not valid arguments")
    void testGetAll_ValidPageAndSize_ReturnsPageOfBookDtos() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookServiceImpl.getAll(0, 10);
        });
        assertEquals("Page and size must be greater than 0", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            bookServiceImpl.getAll(1, 0);
        });
        assertEquals("Page and size must be greater than 0", exception.getMessage());
    }


    @Test
    @DisplayName("Get Given valid arguments")
    void testGetByIsbn_ExistingIsbn_ReturnsBookDto() {
        String isbn = "1234567890";
        BookEntity bookEntity = new BookEntity(1L, isbn, "Title 1", new BigDecimal("0.00"), new BigDecimal("0.00"));
        BookDto expectedDto = new BookDto(1L, isbn, "Title 1", new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"));

        when(bookRepository.findByIsbn(isbn)).thenReturn(java.util.Optional.of(bookEntity));

        BookDto result = bookServiceImpl.getByIsbn(isbn);

        assertEquals(expectedDto, result);
    }

    @Test
    @DisplayName("Get Given not valid arguments")
    void testGetByIsbn_NonExistingIsbn_ThrowsResourceNotFoundException() {
        String isbn = "nonexistent_isbn";
        when(bookRepository.findByIsbn(isbn)).thenReturn(java.util.Optional.empty());
        Exception exception = assertThrows(com.example.demo2.Domain.exception.ResourceNotFoundException.class, () -> {
            bookServiceImpl.getByIsbn(isbn);
        });
        assertEquals("Book with isbn nonexistent_isbn not found", exception.getMessage());
    }

    @Test
    @DisplayName("Create Given valid arguments")
    void testFindByIsbn_ExistingIsbn_ReturnsOptionalBookDto() {
        String isbn = "1234567890";
        BookEntity bookEntity = new BookEntity(1L, isbn, "Title 1", new BigDecimal("0.00"), new BigDecimal("0.00"));
        BookDto expectedDto = new BookDto(1L, isbn, "Title 1", new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"));
        when(bookRepository.findByIsbn(isbn)).thenReturn(java.util.Optional.of(bookEntity));
        java.util.Optional<BookDto> result = bookServiceImpl.findByIsbn(isbn);
        assertTrue(result.isPresent());
        assertEquals(expectedDto, result.get());
    }
    @Test
    @DisplayName("Create Given not valid arguments")
    void testFindByIsbn_NonExistingIsbn_ReturnsEmptyOptional() {
        String isbn = "nonexistent_isbn";
        when(bookRepository.findByIsbn(isbn)).thenReturn(java.util.Optional.empty());
        java.util.Optional<BookDto> result = bookServiceImpl.findByIsbn(isbn);
        assertFalse(result.isPresent());
    }
    @Test
    @DisplayName("Create Given duplicate isbn")
    void testCreate_DuplicateIsbn_ThrowsBusinessException() {
        BookDto bookDto = new BookDto(null, "1234567890", "Title 1", new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"));
        when(bookRepository.findByIsbn(bookDto.isbn())).thenReturn(java.util.Optional.of(new BookEntity(bookDto.id(), bookDto.isbn(), bookDto.titleEs(), bookDto.basePrice(), bookDto.discountPercentage())));
        Exception exception = assertThrows(com.example.demo2.Domain.exception.BusinessException.class, () -> {
            bookServiceImpl.create(bookDto);
        });
        assertEquals("Book with isbn 1234567890 already exists", exception.getMessage());
    }
    @Test
    @DisplayName("update Given valid arguments")
    void testCreate_ValidBookDto_ReturnsCreatedBookDto() {
        BookDto bookDto = new BookDto(null, "1234567890", "Title 1", new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"));
        BookEntity bookEntity = new BookEntity(null, bookDto.isbn(), bookDto.titleEs(), bookDto.basePrice(), bookDto.discountPercentage());
        BookEntity savedBookEntity = new BookEntity(1L, bookDto.isbn(), bookDto.titleEs(), bookDto.basePrice(), bookDto.discountPercentage());
        BookDto expectedDto = new BookDto(1L, bookDto.isbn(), bookDto.titleEs(), bookDto.basePrice(), bookDto.discountPercentage(), bookDto.price());
        when(bookRepository.findByIsbn(bookDto.isbn())).thenReturn(java.util.Optional.empty());
        when(bookRepository.save(bookEntity)).thenReturn(savedBookEntity);
        BookDto result = bookServiceImpl.create(bookDto);
        assertEquals(expectedDto, result);
    }
    @Test
    @DisplayName("update Given non existing isbn")
    void testUpdate_NonExistingIsbn_ThrowsResourceNotFoundException() {
        BookDto bookDto = new BookDto(null, "nonexistent_isbn", "Title 1", new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"));
        when(bookRepository.findByIsbn(bookDto.isbn())).thenReturn(java.util.Optional.empty());
        Exception exception = assertThrows(com.example.demo2.Domain.exception.ResourceNotFoundException.class, () -> {
            bookServiceImpl.update(bookDto);
        });
        assertEquals("Book with isbn nonexistent_isbn not found", exception.getMessage());


    }

}