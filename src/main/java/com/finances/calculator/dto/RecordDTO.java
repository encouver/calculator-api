package com.finances.calculator.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Marcos Ramirez
 */
@Data
public class RecordDTO {
    private Long id;

    public Long userId;

    public UserDTO user;

    public Long operationId;

    public OperationDTO operation;

    public String operationType;

    public Double amount;

    public Double userBalance;

    public String operationResponse;

    public LocalDateTime date;

}
