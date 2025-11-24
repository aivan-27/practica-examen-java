package com.example.demo2.Domain.service.impl;

import com.example.demo2.Domain.exception.BusinessException;
import com.example.demo2.Domain.exception.ResourceNotFoundException;
import com.example.demo2.Domain.mapper.BookMapper;
import com.example.demo2.Domain.model.Page;
import com.example.demo2.Domain.repository.BookRepository;
import com.example.demo2.Domain.repository.entity.BookEntity;
import com.example.demo2.Domain.service.BookService;
import com.example.demo2.Domain.service.dto.BookDto;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Page<BookDto> getAll(int page, int size) {
        if(page < 1 || size < 1) {
            throw new IllegalArgumentException("Page and size must be greater than 0");
        }
        Page<BookEntity> bookEntityPage =  bookRepository
                .findAll(page, size);
        List<BookDto> itemsDto = bookEntityPage.data()
                .stream()
                .map(BookMapper.getInstance()::fromBookEntityToBook)
                .map(BookMapper.getInstance()::fromBookToBookDto)
                .toList();
        return new Page<>(
                itemsDto,
                bookEntityPage.pageNumber(),
                bookEntityPage.pageSize(),
                bookEntityPage.totalElements()
        );


    }

    @Override
    public BookDto getByIsbn(String isbn) {
        return bookRepository
                .findByIsbn(isbn)
                .map(BookMapper.getInstance()::fromBookEntityToBook)
                .map(BookMapper.getInstance()::fromBookToBookDto)
                .orElseThrow(() -> new ResourceNotFoundException("Book with isbn " + isbn + " not found"));
    }

    @Override
    public Optional<BookDto> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(BookMapper.getInstance()::fromBookEntityToBook)
                .map(BookMapper.getInstance()::fromBookToBookDto);
    }

    @Override
    public BookDto create(BookDto bookDto) {
        if (findByIsbn(bookDto.isbn()).isPresent()) {
            throw new BusinessException("Book with isbn " + bookDto.isbn() + " already exists");
        }
        BookEntity newBookEntity = BookMapper.getInstance()
                .fromBookToBookEntity(
                        BookMapper.getInstance()
                                .fromBookDtoToBook(bookDto)
                );

        return BookMapper.getInstance().fromBookToBookDto(
                BookMapper.getInstance().fromBookEntityToBook(
                        bookRepository.save(newBookEntity)
                )
        );
    }

    @Override
    public BookDto update(BookDto bookDto) {
        BookEntity existingBookEntity = bookRepository.findByIsbn(bookDto.isbn())
                .orElseThrow(() -> new ResourceNotFoundException("Book with isbn " + bookDto.isbn() + " not found"));

        BookEntity bookEntityToUpdate = BookMapper.getInstance()
                .fromBookToBookEntity(
                        BookMapper.getInstance()
                                .fromBookDtoToBook(bookDto)
                );

        // Ensure the ID remains the same
        bookEntityToUpdate = new BookEntity(
                existingBookEntity.id(),
                bookEntityToUpdate.isbn(),
                bookEntityToUpdate.titleEs(),
                bookEntityToUpdate.basePrice(),
                bookEntityToUpdate.discountPercentage()
        );

        return BookMapper.getInstance().fromBookToBookDto(
                BookMapper.getInstance().fromBookEntityToBook(
                        bookRepository.save(bookEntityToUpdate)
                )
        );

    }

    @Override
    public void deleteByIsbn(String isbn) {
        BookEntity existingBookEntity = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book with isbn " + isbn + " not found"));
        bookRepository.deleteByIsbn(isbn);

    }
}
