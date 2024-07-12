package com.finances.calculator.dto;

import lombok.Data;

/**
 * @author Marcos Ramirez
 */
@Data
public class OperationDTO {
    public Long id;

    public String type;

    public Double cost;

    public Double a;

    public Double b;

}
