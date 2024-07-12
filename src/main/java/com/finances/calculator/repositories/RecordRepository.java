package com.finances.calculator.repositories;


import com.finances.calculator.entities.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author Marcos Ramirez
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    Page<Record> findByOperationResponseContainingIgnoreCase(String search, Pageable pageable);

    Collection<Record> findByUserId(Long id, Sort date);

}