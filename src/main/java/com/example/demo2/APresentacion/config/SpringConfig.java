package com.example.demo2.APresentacion.config;

import com.example.demo2.Domain.repository.BookRepository;
import com.example.demo2.Domain.service.BookService;
import com.example.demo2.Domain.service.impl.BookServiceImpl;
import com.example.demo2.persistence.dao.jpa.BookJpaDao;
import com.example.demo2.persistence.dao.jpa.Impl.BookJpaDaoImpl;
import com.example.demo2.persistence.repository.BookRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class SpringConfig {

    /************* BOOK *************/

    /*@Bean
    public BookRepository bookRepository(BookJpaDao bookJpaDao, BookRedisDao bookRedisDao, AuthorJpaDao authorJpaDao) {
        return new BookRepositoryImpl(bookJpaDao, bookRedisDao, authorJpaDao);
    }*/
    @Bean
    public BookRepository bookRepository(BookJpaDao bookJpaDao) {
        return new BookRepositoryImpl(bookJpaDao);
    }

    @Bean
    public BookService bookService(BookRepository bookRepository) {
        return new BookServiceImpl(bookRepository);
    }

    @Bean
    public BookJpaDao bookJpaDao() {
        return new BookJpaDaoImpl();
    }
}
