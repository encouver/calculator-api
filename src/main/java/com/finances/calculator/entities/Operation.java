package com.finances.calculator.entities;

import com.finances.calculator.enums.OperationTypesEnum;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Marcos Ramirez
 */
@Entity
@Table(name="operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Operation extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('addition', 'subtraction', 'multiplication', 'division', 'square_root', 'random_string')")
    private OperationTypesEnum type;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double cost;

}
