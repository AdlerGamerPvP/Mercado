package org.dt2.mercado.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "O nome é obrigatório.") @Size(max = 100) String nome,
        @NotBlank(message = "O e-mail é obrigatório.") @Email(message = "Informe um e-mail válido.") @Size(max = 150) String email) { }
