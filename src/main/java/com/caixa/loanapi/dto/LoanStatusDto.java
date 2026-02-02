package com.caixa.loanapi.dto;

import com.caixa.loanapi.entity.LoanStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanStatusDto {
    @NotNull(message = "El estado es obligatorio")
    private LoanStatus status;
}
