package com.example.demo2.persistence.repository;


import com.example.demo2.Domain.repository.entity.BookEntity;
import com.example.demo2.persistence.dao.entity.BookJpaEntity;
import com.example.demo2.persistence.dao.jpa.BookJpaDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookRepositoryImplTest {
    @Mock
    private BookJpaDao bookJpaDao;

    @InjectMocks
    private BookRepositoryImpl bookRepositoryImpl;

    @Test
    @DisplayName("Test find by ISBN")
    void findByIsbnTest() {
        // Given
        String isbn = "978-3-16-148410-0";
        BookJpaEntity jpaEntity = new BookJpaEntity(
                1L,
                isbn,
                "Sample Book",
                BigDecimal.valueOf(29.99),
                BigDecimal.valueOf(10.00)
        );

        when(bookJpaDao.findByIsbn(isbn)).thenReturn(Optional.of(jpaEntity));

        // When
        Optional<BookEntity> result = bookRepositoryImpl.findByIsbn(isbn);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().isbn()).isEqualTo(isbn);
        verify(bookJpaDao, times(1)).findByIsbn(isbn);
    }



}
