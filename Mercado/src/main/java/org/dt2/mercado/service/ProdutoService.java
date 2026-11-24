package org.dt2.mercado.service;
import org.dt2.mercado.dto.ProdutoRequest;
import org.dt2.mercado.exception.RecursoNaoEncontradoException;
import org.dt2.mercado.model.Produto;
import org.dt2.mercado.repository.CategoriaRepository;
import org.dt2.mercado.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;
    public ProdutoService(ProdutoRepository repository, CategoriaRepository categoriaRepository) { this.repository = repository; this.categoriaRepository = categoriaRepository; }
    public List<Produto> listar() { return repository.findAll(); }
    public Produto buscar(Long id) { return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id)); }
    public Produto criar(ProdutoRequest request) { Produto p = new Produto(); aplicar(p, request); return repository.save(p); }
    public Produto atualizar(Long id, ProdutoRequest request) { Produto p = buscar(id); aplicar(p, request); return repository.save(p); }
    private void aplicar(Produto p, ProdutoRequest r) { p.setNome(r.nome().trim()); p.setDescricao(r.descricao()); p.setCategoria(r.categoriaId() == null ? null : categoriaRepository.findById(r.categoriaId()).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada: " + r.categoriaId()))); }
    public void excluir(Long id) { repository.delete(buscar(id)); }
}
