package org.dt2.mercado.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record ProdutoRequest(@NotBlank(message = "O nome do produto é obrigatório.") @Size(max = 120) String nome,
                            @Size(max = 250) String descricao, Long categoriaId) { }
