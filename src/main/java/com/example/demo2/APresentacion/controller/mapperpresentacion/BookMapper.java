package com.example.demo2.APresentacion.controller.mapperpresentacion;

import com.example.demo2.APresentacion.controller.webModel.request.BookInsertRequest;
import com.example.demo2.APresentacion.controller.webModel.request.BookUpdateRequest;
import com.example.demo2.APresentacion.controller.webModel.response.BookDetailResponse;
import com.example.demo2.APresentacion.controller.webModel.response.BookSummaryResponse;
import com.example.demo2.Domain.service.dto.BookDto;

public class BookMapper {
    public static BookSummaryResponse fromBookDtoToBookSummaryResponse(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }
        return new BookSummaryResponse(
                bookDto.isbn(),
                bookDto.titleEs(),
                bookDto.basePrice(),
                bookDto.discountPercentage(),
                bookDto.price()
        );
    }

    public static BookDetailResponse fromBookDtoToBookDetailResponse(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }
        return new BookDetailResponse(
                bookDto.id(),
                bookDto.isbn(),
                bookDto.titleEs(),
                bookDto.basePrice(),
                bookDto.discountPercentage(),
                bookDto.price()
        );
    }

    public static BookDto fromBookInsertRequestToBookDto(BookInsertRequest bookInsertRequest) {
        if (bookInsertRequest == null) {
            return null;
        }
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return new BookDto(
                null,
                bookInsertRequest.isbn(),
                bookInsertRequest.titleEs(),
                bookInsertRequest.basePrice(),
                bookInsertRequest.discountPercentage(),
                null
        );
    }

    public static BookDto fromBookUpdateRequestToBookDto(BookUpdateRequest bookUpdateRequest) {
        if (bookUpdateRequest == null) {
            return null;
        }
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return new BookDto(
                bookUpdateRequest.id(),
                bookUpdateRequest.isbn(),
                bookUpdateRequest.titleEs(),
                bookUpdateRequest.basePrice(),
                bookUpdateRequest.discountPercentage(),
                null
        );
    }
}

