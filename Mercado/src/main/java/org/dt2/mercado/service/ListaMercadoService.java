package org.dt2.mercado.service;
import org.dt2.mercado.dto.ListaMercadoRequest;
import org.dt2.mercado.exception.RecursoNaoEncontradoException;
import org.dt2.mercado.model.ListaMercado;
import org.dt2.mercado.repository.ListaMercadoRepository;
import org.dt2.mercado.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ListaMercadoService {
    private final ListaMercadoRepository repository;
    private final UsuarioRepository usuarioRepository;
    public ListaMercadoService(ListaMercadoRepository repository, UsuarioRepository usuarioRepository) { this.repository = repository; this.usuarioRepository = usuarioRepository; }
    public List<ListaMercado> listar() { return repository.findAll(); }
    public ListaMercado buscar(Long id) { return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Lista não encontrada: " + id)); }
    public ListaMercado criar(ListaMercadoRequest request) { ListaMercado l = new ListaMercado(); aplicar(l, request); return repository.save(l); }
    public ListaMercado atualizar(Long id, ListaMercadoRequest request) { ListaMercado l = buscar(id); aplicar(l, request); return repository.save(l); }
    private void aplicar(ListaMercado l, ListaMercadoRequest r) { l.setNome(r.nome().trim()); l.setUsuario(usuarioRepository.findById(r.usuarioId()).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + r.usuarioId()))); }
    public void excluir(Long id) { repository.delete(buscar(id)); }
}
