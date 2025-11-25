package com.example.demo2.persistence.dao.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "books")
public class BookJpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String isbn;
    @Column(name = "title_es")
    private String titleEs;
    @Column(name = "base_price")
    private java.math.BigDecimal basePrice;
    @Column(name = "discount_percentage")
    private java.math.BigDecimal discountPercentage;



    public BookJpaEntity() {
    }
    public BookJpaEntity( Long id, String isbn, String titleEs, java.math.BigDecimal basePrice, java.math.BigDecimal discountPercentage) {
        this.id = id;
        this.isbn = isbn;
        this.titleEs = titleEs;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
    }
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitleEs() {
        return titleEs;
    }

    public void setTitleEs(String titleEs) {
        this.titleEs = titleEs;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BookJpaEntity other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
}
