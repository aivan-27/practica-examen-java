package com.example.demo2.Domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.Flow;

public class Book {
    private final Long id;
    private final String isbn;
    private final String titleEs;
    private final BigDecimal basePrice;
    private final BigDecimal discountPercentage;
    private final BigDecimal price;

    public Book(
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
        this.price = calculateFinalPrice();
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }


    public String getTitle() {
        return titleEs;
    }

    public String getTitleEs() {
        return titleEs;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }


    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }


    public BigDecimal getPrice() {
        return price;
    }





    public BigDecimal calculateFinalPrice() {
        if( basePrice == null ) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal discount = basePrice
                .multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return basePrice.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }









    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book book)) return false;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

