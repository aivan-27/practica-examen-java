package com.example.demo2.Persistance.repository;

import com.example.demo2.Domain.model.Page;
import com.example.demo2.Domain.repository.BookRepository;
import com.example.demo2.Domain.repository.entity.BookEntity;
import com.example.demo2.Persistance.dao.jpa.BookJpaDao;
import com.example.demo2.Persistance.mapper.mapperpersistance;

import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements BookRepository {
    private final BookJpaDao bookJpaDao;

    public BookRepositoryImpl(BookJpaDao bookJpaDao) {
        this.bookJpaDao = bookJpaDao;
    }


    @Override
    public Page<BookEntity> findAll(int page, int size) {
        List<BookEntity> items = bookJpaDao.findAll(page, size)
                .stream()
                .map(mapperpersistance.getInstance()::fromBookJpaEntityToBookEntity)
                .toList();
        long totalElements = bookJpaDao.count();
        return new Page<>(items, page, size, totalElements);

    }

    @Override
    public Optional<BookEntity> findByIsbn(String isbn) {
        return bookJpaDao.findByIsbn(isbn)
                .map(mapperpersistance.getInstance()::fromBookJpaEntityToBookEntity);

    }

    @Override
    public BookEntity save(BookEntity bookEntity) {
        var bookJpaEntity = mapperpersistance.getInstance().fromBookEntityToBookJpaEntity(bookEntity);
        var savedBookJpaEntity = bookJpaDao.insert(bookJpaEntity);
        return mapperpersistance.getInstance().fromBookJpaEntityToBookEntity(savedBookJpaEntity);
    }

    @Override
    public Optional<BookEntity> findById(Long id) {
        return bookJpaDao.findById(id)
                .map(mapperpersistance.getInstance()::fromBookJpaEntityToBookEntity);
    }

    @Override
    public void deleteByIsbn(String isbn) {
        bookJpaDao.deleteByIsbn(isbn);

    }
}
