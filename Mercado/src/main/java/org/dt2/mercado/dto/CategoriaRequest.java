package org.dt2.mercado.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(@NotBlank(message = "O nome da categoria é obrigatório.") @Size(max = 80) String nome,
                              @Size(max = 250) String descricao) { }
