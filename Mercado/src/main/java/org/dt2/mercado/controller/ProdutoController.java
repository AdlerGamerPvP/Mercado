package org.dt2.mercado.controller;
import jakarta.validation.Valid;
import org.dt2.mercado.dto.ProdutoRequest;
import org.dt2.mercado.model.Produto;
import org.dt2.mercado.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService service;
    public ProdutoController(ProdutoService service) { this.service = service; }
    @GetMapping public List<Produto> listar() { return service.listar(); }
    @GetMapping("/{id}") public Produto buscar(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Produto criar(@Valid @RequestBody ProdutoRequest request) { return service.criar(request); }
    @PutMapping("/{id}") public Produto atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) { return service.atualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { service.excluir(id); }
}
