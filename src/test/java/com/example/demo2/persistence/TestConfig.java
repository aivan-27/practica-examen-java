package com.example.demo2.persistence;

import com.example.demo2.persistence.dao.jpa.BookJpaDao;
import com.example.demo2.persistence.dao.jpa.Impl.BookJpaDaoImpl;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("test")
@EnableJpaRepositories(basePackages = "com.example.demo2.persistence.dao.jpa")
@EntityScan(basePackages = "com.example.demo2.persistence.dao.entity") // CORREGIDO
public class TestConfig {


    @Bean
    public BookJpaDao bookJpaDao(EntityManager entityManager) {
        return new BookJpaDaoImpl();
    }
}
