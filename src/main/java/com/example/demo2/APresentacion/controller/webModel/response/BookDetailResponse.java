package com.example.demo2.APresentacion.controller.webModel.response;

import java.math.BigDecimal;

public record BookDetailResponse(
        Long id,
        String isbn,
        String titleEs,
        BigDecimal basePrice,
        BigDecimal discountPercentage,
        BigDecimal price

)
{}