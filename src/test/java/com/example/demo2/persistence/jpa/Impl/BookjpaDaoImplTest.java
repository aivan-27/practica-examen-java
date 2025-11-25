package com.example.demo2.persistence.jpa.Impl;

import com.example.demo2.persistence.dao.entity.BookJpaEntity;
import com.example.demo2.persistence.dao.jpa.BookJpaDao;
import com.example.demo2.persistence.TestConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BookjpaDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BookJpaDao  bookJpaDao; // sustituir por la implementación real del DAO

    @Test
    @DisplayName("Insert Book - JPA")
    void insertBookjpaTest() {
        // Given
        BookJpaEntity jpaEntity = new BookJpaEntity(
                null,
                "978-3-16-148410-0",
                "Sample Book Title",
                new BigDecimal("29.99"),
                new BigDecimal("10.00")
        );


        // When
        bookJpaDao.insert(jpaEntity);
        assertThat(jpaEntity.getId()).isNotNull();


    }
    @Test
    @DisplayName("Find Book by ID - JPA")
    void findBookByIdjpaTest() {


        BookJpaEntity newBook = new BookJpaEntity(
                null,
                "666666666666",
                "New Book Title ES",
                BigDecimal.valueOf(29.99),
                BigDecimal.valueOf(5.00)
        );

        String sql = "SELECT COUNT(b) FROM BookJpaEntity b";
        long countBefore = entityManager.createQuery(sql, Long.class)
                .getSingleResult();

        BookJpaEntity result = bookJpaDao.insert(newBook);

        long countAfter = entityManager.createQuery(sql, Long.class)
                .getSingleResult();

        long lastId = entityManager.createQuery("SELECT MAX(b.id) FROM BookJpaEntity b", Long.class)
                .getSingleResult();


        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(lastId, result.getId()),
                () -> assertEquals(newBook.getIsbn(), result.getIsbn()),
                () -> assertEquals(newBook.getTitleEs(), result.getTitleEs()),
                () -> assertEquals(newBook.getBasePrice(), result.getBasePrice()),
                () -> assertEquals(newBook.getDiscountPercentage(), result.getDiscountPercentage()),
                () -> assertEquals(countBefore + 1, countAfter)
        );
    }
        @Test
        @DisplayName("Find All Books - JPA")
        void findAllBooksjpaTest() {
            // Given
            BookJpaEntity book1 = new BookJpaEntity(
                    null,
                    "1111111111111",
                    "First Book Title ES",
                    BigDecimal.valueOf(19.99),
                    BigDecimal.valueOf(5.00)
            );
            BookJpaEntity book2 = new BookJpaEntity(
                    null,
                    "2222222222222",
                    "Second Book Title ES",
                    BigDecimal.valueOf(29.99),
                    BigDecimal.valueOf(10.00)
            );
            bookJpaDao.insert(book1);
            bookJpaDao.insert(book2);
            // When
            Set<BookJpaEntity> books = bookJpaDao.findAll(1,10).stream().collect(Collectors.toSet());
            // Then
            assertAll(
                    () -> assertNotNull(books),
                    () -> assertTrue(books.size() >=2),
                    () -> assertTrue(books.stream().anyMatch(b -> b.getIsbn().equals("1111111111111"))),
                    () -> assertTrue(books.stream().anyMatch(b -> b.getIsbn().equals("2222222222222")))
            );
        }
        @Test
        @DisplayName("Delete Book by ISBN - JPA")
        void deleteBookByIsbnjpaTest() {
            // Given
            BookJpaEntity bookToDelete = new BookJpaEntity(
                    null,
                    "3333333333333",
                    "Book To Delete ES",
                    BigDecimal.valueOf(14.99),
                    BigDecimal.valueOf(0.00)
            );
            bookJpaDao.insert(bookToDelete);
            // When
            bookJpaDao.deleteByIsbn("3333333333333");

            BookJpaEntity deletedBook = entityManager.createQuery(
                    "SELECT b FROM BookJpaEntity b WHERE b.isbn = :isbn", BookJpaEntity.class)
                    .setParameter("isbn", "3333333333333")
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            // Then
            assertNull(deletedBook);





        }
        @Test
        @DisplayName("Update Book - JPA")
        void updateBookjpaTest() {
            // Given
            BookJpaEntity bookToUpdate = new BookJpaEntity(
                    null,
                    "4444444444444",
                    "Original Book Title ES",
                    BigDecimal.valueOf(39.99),
                    BigDecimal.valueOf(15.00)
            );
            BookJpaEntity insertedBook = bookJpaDao.insert(bookToUpdate);
            // When
            insertedBook.setTitleEs("Updated Book Title ES");
            insertedBook.setBasePrice(BigDecimal.valueOf(34.99));
            bookJpaDao.update(insertedBook);
            BookJpaEntity updatedBook = entityManager.find(BookJpaEntity.class, insertedBook.getId());
            // Then
            assertAll(
                    () -> assertNotNull(updatedBook),
                    () -> assertEquals("Updated Book Title ES", updatedBook.getTitleEs()),
                    () -> assertEquals(BigDecimal.valueOf(34.99), updatedBook.getBasePrice()
            )
            );
        }

        @Test
        @DisplayName("Find Book by ISBN - JPA")
        void findBookByIsbnjpaTest() {
            // Given
            BookJpaEntity book = new BookJpaEntity(
                    null,
                    "5555555555555",
                    "Find Me Book Title ES",
                    BigDecimal.valueOf(49.99),
                    BigDecimal.valueOf(20.00)
            );
            bookJpaDao.insert(book);
            // When

            BookJpaEntity foundBook = bookJpaDao.findByIsbn("5555555555555").orElse(null);

            // Then
            assertAll(
                    () -> assertNotNull(foundBook),
                    () -> assertEquals("Find Me Book Title ES", foundBook.getTitleEs()),
                    () -> assertEquals(BigDecimal.valueOf(49.99), foundBook.getBasePrice())
            );
        }



    }
