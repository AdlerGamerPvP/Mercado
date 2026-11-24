package org.dt2.mercado.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record ListaMercadoRequest(@NotBlank(message = "O nome da lista é obrigatório.") @Size(max = 120) String nome,
                                @NotNull(message = "Selecione o usuário responsável.") Long usuarioId) { }
