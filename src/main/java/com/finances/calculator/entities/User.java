package com.finances.calculator.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Marcos Ramirez
 */
@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String username;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(nullable = false, columnDefinition = "ENUM('active', 'inactive')")
    private String status;

}
