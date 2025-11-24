package com.example.demo2.Domain.repository.entity;

import java.math.BigDecimal;

public record BookEntity(
        Long id,
        String isbn,
        String titleEs,
        BigDecimal basePrice,
        BigDecimal discountPercentage
) {

    public BookEntity(
            Long id,
            String isbn,
            String titleEs,
            BigDecimal basePrice,
            BigDecimal discountPercentage
    ) {
        this.id = id;
        this.isbn = isbn;
        this.titleEs = titleEs;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
    }
}
