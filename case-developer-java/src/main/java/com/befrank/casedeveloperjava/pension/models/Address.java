package com.befrank.casedeveloperjava.pension.models;

import jakarta.persistence.Embeddable;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    private String street;
    private Integer number;
    private String city;
}
