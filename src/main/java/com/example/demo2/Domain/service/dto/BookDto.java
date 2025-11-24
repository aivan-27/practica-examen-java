package com.example.demo2.Domain.service.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

import java.math.BigDecimal;


public record BookDto(
        Long id,
        @NotNull(message = "ISBN es obligatorio")
        @Size(min = 10, max = 13, message = "El ISBN debe tener entre 10 y 13 caracteres")
        String isbn,
        String titleEs,
        @NotNull(message = "El precio base no puede ser nulo")
        BigDecimal basePrice,
        @NotNull
        @DecimalMax(value = "100.0", inclusive = true, message = "El descuento no puede ser mayor a 100")
        BigDecimal discountPercentage,
        BigDecimal price
) {
    public BookDto(
            Long id,
            String isbn,
            String titleEs,
            BigDecimal basePrice,
            BigDecimal discountPercentage,
            BigDecimal price
    ) {
        this.id = id;
        this.isbn = isbn;
        this.titleEs = titleEs;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.price = price;
    }
}


