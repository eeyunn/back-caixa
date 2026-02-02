package com.caixa.loanapi.service;

import com.caixa.loanapi.dto.LoanRequestDto;
import com.caixa.loanapi.entity.Loan;
import com.caixa.loanapi.entity.LoanStatus;
import com.caixa.loanapi.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan createLoan(LoanRequestDto dto) {
        Loan loan = new Loan();
        loan.setApplicantName(dto.getApplicantName());
        loan.setAmount(dto.getAmount());
        loan.setCurrency(dto.getCurrency());
        loan.setDocumentId(dto.getDocumentId());
        loan.setStatus(LoanStatus.PENDING);
        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public Loan updateLoanStatus(Long id, LoanStatus newStatus) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        validateStateTransition(loan.getStatus(), newStatus);

        loan.setStatus(newStatus);
        return loanRepository.save(loan);
    }

    private void validateStateTransition(LoanStatus current, LoanStatus next) {
        if (current == next) return;

        boolean isValid = false;

        switch (current) {
            case PENDING:
                if (next == LoanStatus.APPROVED || next == LoanStatus.REJECTED) {
                    isValid = true;
                }
                break;
            case APPROVED:
                if (next == LoanStatus.CANCELLED) {
                    isValid = true;
                }
                break;
            case REJECTED:
            case CANCELLED:
                // Terminal states
                isValid = false;
                break;
        }

        if (!isValid) {
            throw new IllegalArgumentException("Invalid state transition from " + current + " to " + next);
        }
    }
}
