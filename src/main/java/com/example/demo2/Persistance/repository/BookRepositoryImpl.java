package com.example.demo2.Persistance.repository;

import com.example.demo2.Persistance.dao.jpa.BookJpaDao;

public class BookRepositoryImpl {
    private final BookJpaDao bookJpaDao;

    public BookRepositoryImpl(BookJpaDao bookJpaDao) {
        this.bookJpaDao = bookJpaDao;
    }
}
