package com.example.demo2.APresentacion.controller.webModel;

import com.example.demo2.APresentacion.controller.mapperpresentacion.BookMapper;
import com.example.demo2.APresentacion.controller.webModel.request.BookInsertRequest;
import com.example.demo2.APresentacion.controller.webModel.request.BookUpdateRequest;
import com.example.demo2.APresentacion.controller.webModel.response.BookDetailResponse;
import com.example.demo2.APresentacion.controller.webModel.response.BookSummaryResponse;
import com.example.demo2.Domain.model.Page;
import com.example.demo2.Domain.service.BookService;
import com.example.demo2.Domain.service.dto.BookDto;
import com.example.demo2.Domain.validation.spring_validator.DtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Page<BookSummaryResponse>> findAllBooks(@RequestParam(required = false, defaultValue = "1") int page,
                                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        Page<BookDto> bookDtoPage = bookService.getAll(page, size);

        List<BookSummaryResponse> bookSummaries = bookDtoPage.data().stream()
                .map(BookMapper::fromBookDtoToBookSummaryResponse)
                .toList();

        Page<BookSummaryResponse> bookSummaryPage = new Page<>(
                bookSummaries,
                bookDtoPage.pageNumber(),
                bookDtoPage.pageSize(),
                bookDtoPage.totalElements()
        );
        return new ResponseEntity<>(bookSummaryPage, HttpStatus.OK);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookDetailResponse> getBookByIsbn(@PathVariable String isbn) {
        BookDetailResponse bookDetailResponse = BookMapper.fromBookDtoToBookDetailResponse(bookService.getByIsbn(isbn));
        return new ResponseEntity<>(bookDetailResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookDetailResponse> createBook(@RequestBody BookInsertRequest bookInsertRequest) {
        BookDto bookDto = BookMapper.fromBookInsertRequestToBookDto(bookInsertRequest);
        DtoValidator.validate(bookDto);
        BookDto createdBook = bookService.create(bookDto);
        return new ResponseEntity<>(BookMapper.fromBookDtoToBookDetailResponse(createdBook), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDetailResponse> updateBook(@PathVariable("id") Long id, @RequestBody BookUpdateRequest bookUpdateRequest) {
        if (!id.equals(bookUpdateRequest.id())) {
            throw new IllegalArgumentException("ID in path and request body must match");
        }
        BookDto bookDto = BookMapper.fromBookUpdateRequestToBookDto(bookUpdateRequest);
        DtoValidator.validate(bookDto);
        BookDto updatedBook = bookService.update(bookDto);
        return new ResponseEntity<>(BookMapper.fromBookDtoToBookDetailResponse(updatedBook), HttpStatus.OK);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable("isbn") String isbn) {
        bookService.deleteByIsbn(isbn);
        return ResponseEntity.noContent().build();
    }
}