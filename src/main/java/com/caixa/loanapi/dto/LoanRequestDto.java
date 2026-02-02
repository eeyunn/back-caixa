package com.caixa.loanapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequestDto {
    @NotBlank(message = "El nombre del solicitante es obligatorio")
    private String applicantName;

    @NotNull(message = "El importe es obligatorio")
    @DecimalMin(value = "0.01", message = "El importe debe ser mayor que 0")
    private BigDecimal amount;

    @NotBlank(message = "La divisa es obligatoria")
    private String currency;

    @NotBlank(message = "El documento es obligatorio")
    private String documentId;
}
