package org.dt2.mercado.controller;
import jakarta.validation.Valid;
import org.dt2.mercado.dto.ItemMercadoRequest;
import org.dt2.mercado.model.ItemMercado;
import org.dt2.mercado.service.ItemMercadoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/itens")
public class ItemMercadoController {
    private final ItemMercadoService service;
    public ItemMercadoController(ItemMercadoService service) { this.service = service; }
    @GetMapping public List<ItemMercado> listar() { return service.listar(); }
    @GetMapping("/{id}") public ItemMercado buscar(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ItemMercado criar(@Valid @RequestBody ItemMercadoRequest request) { return service.criar(request); }
    @PutMapping("/{id}") public ItemMercado atualizar(@PathVariable Long id, @Valid @RequestBody ItemMercadoRequest request) { return service.atualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { service.excluir(id); }
}
