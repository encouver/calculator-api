package com.finances.calculator.controllers;


import com.finances.calculator.dto.OperationDTO;
import com.finances.calculator.dto.OperationResultDTO;
import com.finances.calculator.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * @author Marcos Ramirez
 */
@RestController
@RequestMapping("/api/v1/operations")
public class OperationsControllerV1 {

    @Autowired
    private OperationService operationService;

    @PostMapping("/add")
    public ResponseEntity<OperationResultDTO> add(@RequestBody OperationDTO operationDTO,
                                                  @AuthenticationPrincipal UserDetails userDetails) {

        OperationResultDTO result = new OperationResultDTO();
        try {
            result = operationService.add(operationDTO);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/multiply")
    public ResponseEntity<OperationResultDTO> multiply(@RequestBody OperationDTO operationDTO) {
        OperationResultDTO result = new OperationResultDTO();
        try {
            result = operationService.multiply(operationDTO);

        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/subtract")
    public ResponseEntity<OperationResultDTO> subtract(@RequestBody OperationDTO operationDTO) {
        OperationResultDTO result = new OperationResultDTO();
        try {
            result = operationService.subtract(operationDTO);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/divide")
    public ResponseEntity<OperationResultDTO> divide(@RequestBody OperationDTO operationDTO) {
        OperationResultDTO result = new OperationResultDTO();
        try {
            result = operationService.divide(operationDTO);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/squareRoot")
    public ResponseEntity<OperationResultDTO> squareRoot(@RequestBody OperationDTO operationDTO) {
        OperationResultDTO result = new OperationResultDTO();
        try {
            result = operationService.squareRoot(operationDTO);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/randomString")
    public ResponseEntity<OperationResultDTO> randomString(@RequestBody OperationDTO operationDTO) {
        OperationResultDTO result = new OperationResultDTO();
        try {
            result = operationService.randomString(operationDTO);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(result);
    }

}
