package com.finances.calculator.repositories;


import com.finances.calculator.entities.Operation;
import com.finances.calculator.enums.OperationTypesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Marcos Ramirez
 */
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    Optional<Operation> findByType(OperationTypesEnum type);
}