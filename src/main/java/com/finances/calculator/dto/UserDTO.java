package com.finances.calculator.dto;

import com.finances.calculator.enums.StatusEnum;
import lombok.Data;

/**
 * @author Marcos Ramirez
 */
@Data
public class UserDTO {

    private Long id;

    private String username;

    private String password;

    private StatusEnum status;

}
