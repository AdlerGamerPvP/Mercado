package org.dt2.mercado.service;
import org.dt2.mercado.dto.UsuarioRequest;
import org.dt2.mercado.exception.RecursoNaoEncontradoException;
import org.dt2.mercado.model.Usuario;
import org.dt2.mercado.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    public UsuarioService(UsuarioRepository repository) { this.repository = repository; }
    public List<Usuario> listar() { return repository.findAll(); }
    public Usuario buscar(Long id) { return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id)); }
    public Usuario criar(UsuarioRequest request) { return repository.save(new Usuario(request.nome().trim(), request.email().trim().toLowerCase())); }
    public Usuario atualizar(Long id, UsuarioRequest request) { Usuario usuario = buscar(id); usuario.setNome(request.nome().trim()); usuario.setEmail(request.email().trim().toLowerCase()); return repository.save(usuario); }
    public void excluir(Long id) { repository.delete(buscar(id)); }
}
