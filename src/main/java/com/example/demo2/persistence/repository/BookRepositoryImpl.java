package com.example.demo2.persistence.repository;

import com.example.demo2.Domain.model.Page;
import com.example.demo2.Domain.repository.BookRepository;
import com.example.demo2.Domain.repository.entity.BookEntity;
import com.example.demo2.persistence.dao.entity.BookJpaEntity;
import com.example.demo2.persistence.dao.jpa.BookJpaDao;
import com.example.demo2.persistence.mapper.BookMapper;

import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements BookRepository {
    private final BookJpaDao bookJpaDao;

    public BookRepositoryImpl(BookJpaDao bookJpaDao) {
        this.bookJpaDao = bookJpaDao;
    }


    @Override
    public Page<BookEntity> findAll(int page, int size) {
        List<BookEntity> content = bookJpaDao.findAll(page, size).stream()
                .map(BookMapper.getInstance()::fromBookJpaEntityToBookEntity)
                .toList();
        long totalElements = bookJpaDao.count();
        return new Page<>(content, page, size, totalElements);
    }


    @Override
    public Optional<BookEntity> findByIsbn(String isbn) {
        return bookJpaDao.findByIsbn(isbn)
                .map(BookMapper.getInstance()::fromBookJpaEntityToBookEntity);
    }

    @Override
    public BookEntity save(BookEntity bookEntity) {
        BookJpaEntity bookJpaEntity = BookMapper.getInstance().fromBookEntityToBookJpaEntity(bookEntity);
        if(bookEntity.id() == null) {
            return BookMapper.getInstance().fromBookJpaEntityToBookEntity(bookJpaDao.insert(bookJpaEntity));
        }
        return BookMapper.getInstance().fromBookJpaEntityToBookEntity(bookJpaDao.update(bookJpaEntity));
    }

    @Override
    public Optional<BookEntity> findById(Long id) {
        return bookJpaDao.findById(id)
                .map(BookMapper.getInstance()::fromBookJpaEntityToBookEntity);
    }

    @Override
    public void deleteByIsbn(String isbn) {
        bookJpaDao.deleteByIsbn(isbn);
    }
}
