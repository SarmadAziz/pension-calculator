package com.befrank.casedeveloperjava.pension.repository;

import com.befrank.casedeveloperjava.pension.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
