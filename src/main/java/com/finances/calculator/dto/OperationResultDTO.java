package com.finances.calculator.dto;

import lombok.Data;

/**
 * @author Marcos Ramirez
 */
@Data
public class OperationResultDTO {

    public Double amount;

    public Double userBalance;

    public String operationResponse;

    public String message;

}
