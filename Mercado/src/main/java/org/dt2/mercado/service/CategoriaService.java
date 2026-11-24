package org.dt2.mercado.service;
import org.dt2.mercado.dto.CategoriaRequest;
import org.dt2.mercado.exception.RecursoNaoEncontradoException;
import org.dt2.mercado.model.Categoria;
import org.dt2.mercado.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CategoriaService {
    private final CategoriaRepository repository;
    public CategoriaService(CategoriaRepository repository) { this.repository = repository; }
    public List<Categoria> listar() { return repository.findAll(); }
    public Categoria buscar(Long id) { return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada: " + id)); }
    public Categoria criar(CategoriaRequest request) { return repository.save(new Categoria(request.nome().trim(), request.descricao())); }
    public Categoria atualizar(Long id, CategoriaRequest request) { Categoria c = buscar(id); c.setNome(request.nome().trim()); c.setDescricao(request.descricao()); return repository.save(c); }
    public void excluir(Long id) { repository.delete(buscar(id)); }
}
