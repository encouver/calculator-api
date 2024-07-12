package com.finances.calculator.services;

import com.finances.calculator.authentication.UserDetailsServiceImpl;
import com.finances.calculator.dto.OperationDTO;
import com.finances.calculator.dto.OperationResultDTO;
import com.finances.calculator.dto.RecordDTO;
import com.finances.calculator.entities.Operation;
import com.finances.calculator.enums.OperationTypesEnum;
import com.finances.calculator.repositories.OperationRepository;
import com.finances.calculator.repositories.RecordRepository;
import com.finances.calculator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


/**
 * @author Marcos Ramirez
 */
@Service
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public OperationResultDTO add(OperationDTO operationDTO) throws Exception {

        Optional<Operation> operation = operationRepository.findByType(OperationTypesEnum.addition);
        if (operation.isEmpty()) {
            throw new RuntimeException("Operation not found");
        }
        // validate both numbers are valid
        if (operationDTO.getA() == null || operationDTO.getB() == null) {
            throw new RuntimeException("Invalid input");
        }

        Double amount = (operationDTO.getA() + operationDTO.getB());

        RecordDTO recordDto = new RecordDTO();
        recordDto.setOperationId(operation.get().getId());
        recordDto.setAmount(amount);
        recordDto.setOperationResponse("Successful operation");

        recordDto = recordsService.addRecord(recordDto);

        OperationResultDTO result = new OperationResultDTO();
        result.setAmount(amount);
        result.setUserBalance(recordDto.getUserBalance());
        result.setOperationResponse(recordDto.getOperationResponse());

        return result;
    }

    public OperationResultDTO multiply(OperationDTO operationDTO) throws  Exception {
        Optional<Operation> operation = operationRepository.findByType(OperationTypesEnum.multiplication);
        if (operation.isEmpty()) {
            throw new RuntimeException("Operation not found");
        }

        // validate both numbers are valid
        if (operationDTO.getA() == null || operationDTO.getB() == null) {
            throw new RuntimeException("Invalid input");
        }

        Double amount = operationDTO.getA() * operationDTO.getB();


        RecordDTO recordDto = new RecordDTO();
        recordDto.setOperationId(operation.get().getId());
        recordDto.setAmount(amount);
        recordDto.setOperationResponse("Successful operation");

        recordDto = recordsService.addRecord(recordDto);

        OperationResultDTO result = new OperationResultDTO();
        result.setAmount(amount);
        result.setUserBalance(recordDto.getUserBalance());
        result.setOperationResponse(recordDto.getOperationResponse());
        return result;
    }

    public OperationResultDTO subtract(OperationDTO operationDTO) throws Exception {
        Optional<Operation> operation = operationRepository.findByType(OperationTypesEnum.subtraction);
        if (operation.isEmpty()) {
            throw new RuntimeException("Operation not found");
        }

        // validate both numbers are valid
        if (operationDTO.getA() == null || operationDTO.getB() == null) {
            throw new RuntimeException("Invalid input");
        }

        Double amount = operationDTO.getA() - operationDTO.getB();

        RecordDTO recordDto = new RecordDTO();
        recordDto.setOperationId(operation.get().getId());
        recordDto.setAmount(amount);
        recordDto.setOperationResponse("Successful operation");

        recordDto = recordsService.addRecord(recordDto);

        OperationResultDTO result = new OperationResultDTO();
        result.setAmount(amount);
        result.setUserBalance(recordDto.getUserBalance());
        result.setOperationResponse(recordDto.getOperationResponse());
        return result;
    }

    public OperationResultDTO divide(OperationDTO operationDTO) throws Exception {
        Optional<Operation> operation = operationRepository.findByType(OperationTypesEnum.division);
        if (operation.isEmpty()) {
            throw new RuntimeException("Operation not found");
        }

        // validate both numbers are valid
        if (operationDTO.getA() == null || operationDTO.getB() == null) {
            throw new RuntimeException("Invalid input");
        }

        RecordDTO recordDto = new RecordDTO();
        recordDto.setOperationId(operation.get().getId());

        Double amount = null;

        // validate both numbers are valid
        if (operationDTO.getA() == null || operationDTO.getB() == null) {
            recordDto.setOperationResponse("Invalid input");
        }

        // validate division by zero
        if (operationDTO.getB() == 0) {
            throw new ArithmeticException("Division by zero");
        }else{
            recordDto.setOperationResponse("Successful operation");
            amount = operationDTO.getA() / operationDTO.getB();
        }

        recordDto.setAmount(amount);

        recordDto = recordsService.addRecord(recordDto);

        OperationResultDTO result = new OperationResultDTO();
        result.setAmount(amount);
        result.setUserBalance(recordDto.getUserBalance());
        result.setOperationResponse(recordDto.getOperationResponse());

        return result;

    }

    public OperationResultDTO squareRoot(OperationDTO operationDTO) throws Exception {
        Optional<Operation> operation = operationRepository.findByType(OperationTypesEnum.square_root);
        if (operation.isEmpty()) {
            throw new RuntimeException("Operation not found");
        }

        // validate both numbers are valid
        if (operationDTO.getA() == null ) {
            throw new RuntimeException("Invalid input");
        }

        RecordDTO recordDto = new RecordDTO();
        recordDto.setOperationId(operation.get().getId());

        Double amount = null;

        // validate square root of negative number
        if (operationDTO.getA() < 0) {
            throw new ArithmeticException("Square root of negative number");
        }else{
            recordDto.setOperationResponse("Successful operation");
            amount = Math.sqrt(operationDTO.getA());
        }

        recordDto.setAmount(amount);

        recordDto = recordsService.addRecord(recordDto);

        OperationResultDTO result = new OperationResultDTO();
        result.setAmount(amount);
        result.setUserBalance(recordDto.getUserBalance());
        result.setOperationResponse(recordDto.getOperationResponse());

        return result;
    }

    public OperationResultDTO randomString(OperationDTO operationDTO) throws Exception {
        Optional<Operation> operation = operationRepository.findByType(OperationTypesEnum.random_string);
        if (operation.isEmpty()) {
            throw new RuntimeException("Operation not found");
        }

        RecordDTO recordDto = new RecordDTO();
        recordDto.setOperationId(operation.get().getId());

        // use third party service to generate random string,
        // ie, make a request to a service that generates random strings
        RestTemplate restTemplate = new RestTemplate();
        String RANDOM_ORG_URL = "https://www.random.org/strings/?num=1&len=8&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new";
        try {
            String randomString = restTemplate.getForObject(RANDOM_ORG_URL, String.class);
            recordDto.setOperationResponse(randomString);
        } catch (Exception e) {
            throw new RuntimeException("Error generating random string");
        }

        recordDto.setAmount(0D);

        recordDto = recordsService.addRecord(recordDto);

        OperationResultDTO result = new OperationResultDTO();
        result.setUserBalance(recordDto.getUserBalance());
        result.setOperationResponse(recordDto.getOperationResponse());

        return result;
    }

}