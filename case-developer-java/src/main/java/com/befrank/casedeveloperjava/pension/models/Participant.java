package com.befrank.casedeveloperjava.pension.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.hibernate.validator.constraints.Email;

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

    @NotBlank(message = "naam verplicht")
    private String name;

    @NotNull(message = "geboortedarum verplicht")
    @Past(message = "GEboortedatum moet in het verleden")
    private LocalDate dateOfBirth;

    @Email(message = "Email onjuist")
    private String email;

    @Embedded
    private Address address;

    @NotBlank(message = "rekening is verplicht")
    private String retirementAccount;

    private Boolean isEmployed;

    // todo custom validation voor minimum leeftijd
    @Max(value = 67, message = "Pensioenleeftijd moet onder 67")
    private Integer retirementAge;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_id")
    private Employment employment;
}
