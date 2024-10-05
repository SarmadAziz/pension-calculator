package com.befrank.casedeveloperjava.pension.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Participant {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private LocalDate dateOfBirth;
    private String email;
    @Embedded
    private Address address;
    private String retirementAccount;
    private Boolean isEmployed;
    private Integer retirementAge;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_id")
    private Employment employment;
}
