package com.example.demo2.APresentacion.controller.webModel.request;

import java.math.BigDecimal;

public record BookInsertRequest(
        String isbn,
        String titleEs,
        BigDecimal basePrice,
        BigDecimal discountPercentage
) {
}
