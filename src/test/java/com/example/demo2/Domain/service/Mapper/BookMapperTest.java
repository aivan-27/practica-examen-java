package com.example.demo2.Domain.service.Mapper;

import com.example.demo2.Domain.mapper.BookMapper;
import com.example.demo2.Domain.model.Book;
import com.example.demo2.Domain.repository.entity.BookEntity;
import com.example.demo2.Domain.service.dto.BookDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BookMapperTest {

    @Test
    @DisplayName("Should map BookEntity to Book correctly")
    void fromBookEntityToBookTest() {
        // Given
        BookEntity bookEntity = new BookEntity(
                1L,
                "9781234567890",
                "Test Book Title",
                BigDecimal.valueOf(29.99),
                BigDecimal.valueOf(10.00)
        );

        // When
        Book book = BookMapper.getInstance().fromBookEntityToBook(bookEntity);

        // Then
        assertAll(
                () -> assertNotNull(book),
                () -> assertEquals(bookEntity.id(), book.getId()),
                () -> assertEquals(bookEntity.isbn(), book.getIsbn()),
                () -> assertEquals(bookEntity.titleEs(), book.getTitleEs()),
                () -> assertEquals(bookEntity.basePrice(), book.getBasePrice()),
                () -> assertEquals(bookEntity.discountPercentage(), book.getDiscountPercentage())
        );
    }

    @Test
    @DisplayName("Should map Book to BookEntity correctly")
    void fromBookToBookEntityTest() {
        // Given
        Book book = new Book(
                1L,
                "9781234567890",
                "Test Book Title",
                BigDecimal.valueOf(29.99),
                BigDecimal.valueOf(10.00)
        );

        // When
        BookEntity bookEntity = BookMapper.getInstance().fromBookToBookEntity(book);

        // Then
        assertAll(
                () -> assertNotNull(bookEntity),
                () -> assertEquals(book.getId(), bookEntity.id()),
                () -> assertEquals(book.getIsbn(), bookEntity.isbn()),
                () -> assertEquals(book.getTitleEs(), bookEntity.titleEs()),
                () -> assertEquals(book.getBasePrice(), bookEntity.basePrice()),
                () -> assertEquals(book.getDiscountPercentage(), bookEntity.discountPercentage())
        );
    }

    @Test
    @DisplayName("Should map Book to BookDto correctly with calculated price")
    void fromBookToBookDtoTest() {
        // Given
        Book book = new Book(
                1L,
                "9781234567890",
                "Test Book Title",
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(20.00)
        );

        // When
        BookDto bookDto = BookMapper.getInstance().fromBookToBookDto(book);

        // Then
        assertAll(
                () -> assertNotNull(bookDto),
                () -> assertEquals(book.getId(), bookDto.id()),
                () -> assertEquals(book.getIsbn(), bookDto.isbn()),
                () -> assertEquals(book.getTitleEs(), bookDto.titleEs()),
                () -> assertEquals(book.getBasePrice(), bookDto.basePrice()),
                () -> assertEquals(book.getDiscountPercentage(), bookDto.discountPercentage()),
                () -> assertNotNull(bookDto.price()),
                () -> assertThat(bookDto.price()).isEqualByComparingTo(BigDecimal.valueOf(80.00))
        );
    }

    @Test
    @DisplayName("Should map BookDto to Book correctly")
    void fromBookDtoToBookTest() {
        // Given
        BookDto bookDto = new BookDto(
                1L,
                "9781234567890",
                "Test Book Title",
                BigDecimal.valueOf(29.99),
                BigDecimal.valueOf(10.00),
                BigDecimal.valueOf(26.99)
        );

        // When
        Book book = BookMapper.getInstance().fromBookDtoToBook(bookDto);

        // Then
        assertAll(
                () -> assertNotNull(book),
                () -> assertEquals(bookDto.id(), book.getId()),
                () -> assertEquals(bookDto.isbn(), book.getIsbn()),
                () -> assertEquals(bookDto.titleEs(), book.getTitleEs()),
                () -> assertEquals(bookDto.basePrice(), book.getBasePrice()),
                () -> assertEquals(bookDto.discountPercentage(), book.getDiscountPercentage())
        );
    }

    @Test
    @DisplayName("Should map BookDto to BookEntity correctly")
    void fromBookDtoToBookEntityTest() {
        // Given
        BookDto bookDto = new BookDto(
                1L,
                "9781234567890",
                "Test Book Title",
                BigDecimal.valueOf(29.99),
                BigDecimal.valueOf(10.00),
                BigDecimal.valueOf(26.99)
        );

        // When
        BookEntity bookEntity = BookMapper.getInstance().fromBookDtoToBookEntity(bookDto);

        // Then
        assertAll(
                () -> assertNotNull(bookEntity),
                () -> assertEquals(bookDto.id(), bookEntity.id()),
                () -> assertEquals(bookDto.isbn(), bookEntity.isbn()),
                () -> assertEquals(bookDto.titleEs(), bookEntity.titleEs()),
                () -> assertEquals(bookDto.basePrice(), bookEntity.basePrice()),
                () -> assertEquals(bookDto.discountPercentage(), bookEntity.discountPercentage())
        );
    }

    @Test
    @DisplayName("Should return null when BookEntity is null")
    void fromBookEntityToBookNullTest() {
        // When
        Book book = BookMapper.getInstance().fromBookEntityToBook(null);

        // Then
        assertNull(book);
    }

    @Test
    @DisplayName("Should return null when Book is null - to BookEntity")
    void fromBookToBookEntityNullTest() {
        // When
        BookEntity bookEntity = BookMapper.getInstance().fromBookToBookEntity(null);

        // Then
        assertNull(bookEntity);
    }

    @Test
    @DisplayName("Should return null when Book is null - to BookDto")
    void fromBookToBookDtoNullTest() {
        // When
        BookDto bookDto = BookMapper.getInstance().fromBookToBookDto(null);

        // Then
        assertNull(bookDto);
    }

    @Test
    @DisplayName("Should return null when BookDto is null - to Book")
    void fromBookDtoToBookNullTest() {
        // When
        Book book = BookMapper.getInstance().fromBookDtoToBook(null);

        // Then
        assertNull(book);
    }

    @Test
    @DisplayName("Should return null when BookDto is null - to BookEntity")
    void fromBookDtoToBookEntityNullTest() {
        // When
        BookEntity bookEntity = BookMapper.getInstance().fromBookDtoToBookEntity(null);

        // Then
        assertNull(bookEntity);
    }

    @Test
    @DisplayName("Should calculate final price correctly with discount")
    void calculateFinalPriceWithDiscountTest() {
        // Given
        Book book = new Book(
                1L,
                "9781234567890",
                "Test Book",
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(25.00)
        );

        // When
        BookDto bookDto = BookMapper.getInstance().fromBookToBookDto(book);

        // Then
        assertThat(bookDto.price()).isEqualByComparingTo(BigDecimal.valueOf(75.00));
    }

    @Test
    @DisplayName("Should calculate final price correctly with zero discount")
    void calculateFinalPriceWithZeroDiscountTest() {
        // Given
        Book book = new Book(
                1L,
                "9781234567890",
                "Test Book",
                BigDecimal.valueOf(50.00),
                BigDecimal.ZERO
        );

        // When
        BookDto bookDto = BookMapper.getInstance().fromBookToBookDto(book);

        // Then
        assertThat(bookDto.price()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
    }
}