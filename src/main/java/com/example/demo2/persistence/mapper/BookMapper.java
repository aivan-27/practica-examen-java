package com.example.demo2.persistence.mapper;

import com.example.demo2.Domain.repository.entity.BookEntity;
import com.example.demo2.persistence.dao.entity.BookJpaEntity;

public class BookMapper {
    private static BookMapper INSTANCE;

    private BookMapper() {
    }

    public static BookMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookMapper();
        }
        return INSTANCE;
    }

    public BookEntity fromBookJpaEntityToBookEntity(BookJpaEntity bookJpaEntity) {
        if (bookJpaEntity == null) {
            return null;
        }
        return new BookEntity(
                bookJpaEntity.getId(),
                bookJpaEntity.getIsbn(),
                bookJpaEntity.getTitleEs(),
                bookJpaEntity.getBasePrice(),
                bookJpaEntity.getDiscountPercentage()
        );
    }
    public BookJpaEntity fromBookEntityToBookJpaEntity(BookEntity bookEntity) {
        if (bookEntity == null) {
            return null;
        }
        return new BookJpaEntity(
                bookEntity.id(),
                bookEntity.isbn(),
                bookEntity.titleEs(),
                bookEntity.basePrice(),
                bookEntity.discountPercentage()
        );
    }
}
