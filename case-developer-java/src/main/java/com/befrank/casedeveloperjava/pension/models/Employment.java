package com.befrank.casedeveloperjava.pension.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employment {
    @Id
    @GeneratedValue
    private Long id;

    private Double fullTimeSalary;
    private Double partTimePercentage;
    private Double franchise = 15599.0;
    private Double availablePremiumPercentage = 0.05;
}
