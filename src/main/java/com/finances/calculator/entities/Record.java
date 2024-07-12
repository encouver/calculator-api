package com.finances.calculator.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/**
 * @author Marcos Ramirez
 */
@Entity
@Table(name="records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Record extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    private Operation operation;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double amount;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double userBalance;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String operationResponse = "";

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date = LocalDateTime.now();

}
