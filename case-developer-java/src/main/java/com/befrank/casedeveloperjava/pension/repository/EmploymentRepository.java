package com.befrank.casedeveloperjava.pension.repository;

import com.befrank.casedeveloperjava.pension.models.Employment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, Long> {
}

