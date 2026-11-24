package org.dt2.mercado.controller;
import jakarta.validation.Valid;
import org.dt2.mercado.dto.ListaMercadoRequest;
import org.dt2.mercado.model.ListaMercado;
import org.dt2.mercado.service.ListaMercadoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/listas")
public class ListaMercadoController {
    private final ListaMercadoService service;
    public ListaMercadoController(ListaMercadoService service) { this.service = service; }
    @GetMapping public List<ListaMercado> listar() { return service.listar(); }
    @GetMapping("/{id}") public ListaMercado buscar(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ListaMercado criar(@Valid @RequestBody ListaMercadoRequest request) { return service.criar(request); }
    @PutMapping("/{id}") public ListaMercado atualizar(@PathVariable Long id, @Valid @RequestBody ListaMercadoRequest request) { return service.atualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { service.excluir(id); }
}
