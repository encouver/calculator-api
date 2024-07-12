package com.finances.calculator.services;

import com.finances.calculator.authentication.UserDetailsServiceImpl;
import com.finances.calculator.dto.OperationDTO;
import com.finances.calculator.dto.RecordDTO;
import com.finances.calculator.dto.UserDTO;
import com.finances.calculator.entities.Operation;
import com.finances.calculator.entities.Record;
import com.finances.calculator.entities.User;
import com.finances.calculator.repositories.OperationRepository;
import com.finances.calculator.repositories.RecordRepository;
import com.finances.calculator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Marcos Ramirez
 */
@Service
public class RecordsService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private OperationRepository operationRepository;

    @Transactional
    public Page<RecordDTO> getRecords(int page, int size, String sortBy, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Record> recordsPage;

        if (search != null && !search.isEmpty()) {
            recordsPage = recordRepository.findByOperationResponseContainingIgnoreCase(search, pageable);
        } else {
            recordsPage = recordRepository.findAll(pageable);
        }

        return recordsPage.map(record -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(record.getUser().getId());
            userDTO.setUsername(record.getUser().getUsername());

            OperationDTO operationDTO = new OperationDTO();
            operationDTO.setId(record.getOperation().getId());
            operationDTO.setType(record.getOperation().getType().toString());
            operationDTO.setCost(record.getOperation().getCost());


            RecordDTO recDTO = new RecordDTO();
            recDTO.setId(record.getId());
            recDTO.setUser(userDTO);
            recDTO.setOperation(operationDTO);
            recDTO.setOperationType(record.getOperation().getType().toString());
            recDTO.setUserBalance(record.getUserBalance());
            recDTO.setAmount(record.getAmount());
            recDTO.setOperationResponse(record.getOperationResponse());
            recDTO.setDate(record.getDate());

            return recDTO;
        });
    }

    @Transactional
    public RecordDTO addRecord(RecordDTO recordDTO) {

        Optional<User> user;
        // If the user or operation is not provided, we will use the current user
        if (recordDTO.getUserId() == null || recordDTO.getOperationId() == null) {
            UserDetails userDetails = (UserDetails) userDetailsServiceImpl.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            // Now you can access userDetails.getUsername() or other UserDetails properties
            String username = userDetails.getUsername();

            user = userRepository.findByUsername(username);
        }else {

            user = userRepository.findById(recordDTO.getUserId());
        }

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<Operation> operation = operationRepository.findById(recordDTO.getOperationId());
        if (operation.isEmpty()) {
            throw new RuntimeException("Operation not found");
        }

        Double currentBalance = this.getBalance();

        if (currentBalance < operation.get().getCost()) {
            throw new RuntimeException("Insufficient balance");
        }

        Record record = new Record();

        record.setUser(user.get());
        record.setOperation(operation.get());
        record.setAmount(recordDTO.getAmount());
        record.setOperationResponse(recordDTO.getOperationResponse());
        record.setUserBalance(currentBalance - operation.get().getCost() < 0 ? 0 : currentBalance - operation.get().getCost());

        record = this.addRecord(record);

        recordDTO.setId(record.getId());
        recordDTO.setUserBalance(record.getUserBalance());

        return recordDTO;
    }

    @Transactional
    public Record addRecord(Record record) {

        recordRepository.save(record);

        return record;
    }

    @Transactional
    public Boolean deleteRecord(Long recordId) {

        UserDetails userDetails = (UserDetails) userDetailsServiceImpl.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Optional<User>  user = userRepository.findByUsername(userDetails.getUsername());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<Record> record = recordRepository.findById(recordId);

        if (record.isEmpty()) {
            throw new RuntimeException("Record not found");
        }

        if (!record.get().getUser().getId().equals(user.get().getId())) {
            throw new RuntimeException("Record not found");
        }

       recordRepository.deleteById(recordId);

       return Boolean.TRUE;
    }

    public RecordDTO getRecordById(Long recordId) {
        Optional<Record> recordEntity = recordRepository.findById(recordId);

        if (recordEntity.isPresent()) {
            RecordDTO recordDTO = new RecordDTO();
            recordDTO.setId(recordEntity.get().getId());
            recordDTO.setAmount(recordEntity.get().getAmount());
            recordDTO.setDate(recordEntity.get().getDate());
            return recordDTO;
        }

        return null;
    }

    public Double getBalance() {

        UserDetails userDetails = (UserDetails) userDetailsServiceImpl.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        // Now you can access userDetails.getUsername() or other UserDetails properties
        String username = userDetails.getUsername();

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        final Double[] lastUserBalance = {100D};

        recordRepository.findByUserId(user.get().getId(), Sort.by(Sort.Direction.DESC, "date"))
                .stream().findFirst().ifPresent(record -> {
                    // Update the value inside the mutable wrapper
                    lastUserBalance[0] = record.getUserBalance();
                });

        return lastUserBalance[0];
    }
}