package org.dt2.mercado.controller;
import jakarta.validation.Valid;
import org.dt2.mercado.dto.CategoriaRequest;
import org.dt2.mercado.model.Categoria;
import org.dt2.mercado.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service = service; }
    @GetMapping public List<Categoria> listar() { return service.listar(); }
    @GetMapping("/{id}") public Categoria buscar(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Categoria criar(@Valid @RequestBody CategoriaRequest request) { return service.criar(request); }
    @PutMapping("/{id}") public Categoria atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) { return service.atualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { service.excluir(id); }
}
