package org.dt2.mercado.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "itens_mercado")
public class ItemMercado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lista_id", nullable = false)
    private ListaMercado lista;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
    private Integer quantidade = 1;

    @DecimalMin(value = "0.0", inclusive = true, message = "O preço não pode ser negativo.")
    private Double precoUnitario = 0.0;

    @Column(nullable = false)
    private boolean comprado = false;

    public ItemMercado() { }
    public Long getId() { return id; }
    public ListaMercado getLista() { return lista; }
    public void setLista(ListaMercado lista) { this.lista = lista; }
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public Double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(Double precoUnitario) { this.precoUnitario = precoUnitario; }
    public boolean isComprado() { return comprado; }
    public void setComprado(boolean comprado) { this.comprado = comprado; }
}
