package com.finances.calculator.controllers;

import com.finances.calculator.dto.RecordDTO;
import com.finances.calculator.services.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Marcos Ramirez
 */
@RestController
@RequestMapping("/api/v1/records")
public class RecordsControllerV1 {

    @Autowired
    private RecordsService recordsService;

    @GetMapping("/{recordId}")
    public ResponseEntity<RecordDTO> getRecordById(@PathVariable Long recordId) {

        RecordDTO recordDTO = recordsService.getRecordById(recordId);

        if (recordDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(recordDTO);
    }

    @GetMapping("/")
    public ResponseEntity<Page<RecordDTO>> getRecords( @RequestParam int page,
                                                       @RequestParam int size,
                                                       @RequestParam String sortBy,
                                                       @RequestParam(required = false, defaultValue = "") String search){

        Page<RecordDTO> result = recordsService.getRecords( page, size, sortBy, search);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/")
    public ResponseEntity<RecordDTO> addRecord(@RequestBody RecordDTO recordDTO) {

        RecordDTO result = recordsService.addRecord(recordDTO);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRecord(@PathVariable("id") Long recordId) {

        Boolean result = recordsService.deleteRecord(recordId);

        return ResponseEntity.ok(result);
    }
}


