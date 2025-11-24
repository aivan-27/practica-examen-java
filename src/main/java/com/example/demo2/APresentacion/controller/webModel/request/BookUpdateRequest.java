package com.example.demo2.APresentacion.controller.webModel.request;

import java.math.BigDecimal;

public record BookUpdateRequest (
        Long id,
        String isbn,
        String titleEs,
        BigDecimal basePrice,
        BigDecimal discountPercentage
){
}
