package com.finances.calculator.repositories;


import com.finances.calculator.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Marcos Ramirez
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByUsernameAndPassword(String username, String password);

        Optional<User> findByUsername(String username);
}