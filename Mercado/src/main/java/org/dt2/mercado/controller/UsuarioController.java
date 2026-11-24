package org.dt2.mercado.controller;
import jakarta.validation.Valid;
import org.dt2.mercado.dto.UsuarioRequest;
import org.dt2.mercado.model.Usuario;
import org.dt2.mercado.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) { this.service = service; }
    @GetMapping public List<Usuario> listar() { return service.listar(); }
    @GetMapping("/{id}") public Usuario buscar(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Usuario criar(@Valid @RequestBody UsuarioRequest request) { return service.criar(request); }
    @PutMapping("/{id}") public Usuario atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) { return service.atualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { service.excluir(id); }
}
