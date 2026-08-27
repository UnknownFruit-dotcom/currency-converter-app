package com.example.fruit.converter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "currencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 3)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nameRu;

    @Column(nullable = false, length = 10)
    private String symbol;

    @Column(nullable = false)
    private Short decimalPlaces = 2;

    @Column(nullable = false)
    private Boolean active = true;
}
