package org.dt2.mercado.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public record ItemMercadoRequest(@NotNull(message = "Selecione uma lista.") Long listaId,
                               @NotNull(message = "Selecione um produto.") Long produtoId,
                               @NotNull @Min(value = 1, message = "A quantidade deve ser no mínimo 1.") Integer quantidade,
                               @NotNull @DecimalMin(value = "0.0", message = "O preço não pode ser negativo.") Double precoUnitario,
                               boolean comprado) { }
