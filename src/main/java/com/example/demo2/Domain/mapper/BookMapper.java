package com.example.demo2.Domain.mapper;

import com.example.demo2.Domain.exception.ValidationException;
import com.example.demo2.Domain.model.Book;
import com.example.demo2.Domain.repository.entity.BookEntity;
import com.example.demo2.Domain.service.dto.BookDto;

import java.util.ArrayList;

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

    public Book fromBookEntityToBook(BookEntity bookEntity) {
        if (bookEntity == null) {
            return null;
        }
        try {
            return new Book(
                    bookEntity.id(),
                    bookEntity.isbn(),
                    bookEntity.titleEs(),
                    bookEntity.basePrice(),
                    bookEntity.discountPercentage()

            );
        } catch (ValidationException e) {
            //Añadir al log
            return null;
        }

    }

    public BookEntity fromBookToBookEntity(Book book) {
        if (book == null) {
            return null;
        }
        return new BookEntity(
                book.getId(),
                book.getIsbn(),
                book.getTitleEs(),
                book.getBasePrice(),
                book.getDiscountPercentage()
        );
    }

    public BookDto fromBookToBookDto(Book book) {
        if (book == null) {
            return null;
        }
        return new BookDto(
                book.getId(),
                book.getIsbn(),
                book.getTitleEs(),
                book.getBasePrice(),
                book.getDiscountPercentage(),
                book.calculateFinalPrice()
        );
    }


    public Book fromBookDtoToBook(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }

        return new Book(
                bookDto.id(),
                bookDto.isbn(),
                bookDto.titleEs(),
                bookDto.basePrice(),
                bookDto.discountPercentage()
        );
    }

    public BookEntity fromBookDtoToBookEntity(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }

        return new BookEntity(
                bookDto.id(),
                bookDto.isbn(),
                bookDto.titleEs(),
                bookDto.basePrice(),
                bookDto.discountPercentage()
        );
    }

}
