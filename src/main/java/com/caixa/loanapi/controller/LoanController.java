package com.caixa.loanapi.controller;

import com.caixa.loanapi.dto.LoanRequestDto;
import com.caixa.loanapi.dto.LoanStatusDto;
import com.caixa.loanapi.entity.Loan;
import com.caixa.loanapi.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanRequestDto loanDto) {
        Loan createdLoan = loanService.createLoan(loanDto);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return loanService.getLoanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateLoanStatus(@PathVariable Long id, @Valid @RequestBody LoanStatusDto statusDto) {
        try {
            Loan updatedLoan = loanService.updateLoanStatus(id, statusDto.getStatus());
            return ResponseEntity.ok(updatedLoan);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Loan not found")) {
                return ResponseEntity.notFound().build();
            }
            if (e.getMessage().contains("Invalid state transition")) { // Actually IllegalArgumentException usually
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            // If service throws IllegalArgumentException manually
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Simple Exception Handler for runtime state validation
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
