package org.dt2.mercado.service;
import org.dt2.mercado.dto.ItemMercadoRequest;
import org.dt2.mercado.exception.RecursoNaoEncontradoException;
import org.dt2.mercado.model.ItemMercado;
import org.dt2.mercado.repository.ItemMercadoRepository;
import org.dt2.mercado.repository.ListaCompraRepository;
import org.dt2.mercado.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ItemMercadoService {
    private final ItemMercadoRepository repository;
    private final ListaCompraRepository listaRepository;
    private final ProdutoRepository produtoRepository;
    public ItemMercadoService(ItemMercadoRepository repository, ListaCompraRepository listaRepository, ProdutoRepository produtoRepository) { this.repository = repository; this.listaRepository = listaRepository; this.produtoRepository = produtoRepository; }
    public List<ItemMercado> listar() { return repository.findAll(); }
    public ItemMercado buscar(Long id) { return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado: " + id)); }
    public ItemMercado criar(ItemMercadoRequest request) { ItemMercado item = new ItemMercado(); aplicar(item, request); return repository.save(item); }
    public ItemMercado atualizar(Long id, ItemMercadoRequest request) { ItemMercado item = buscar(id); aplicar(item, request); return repository.save(item); }
    private void aplicar(ItemMercado item, ItemMercadoRequest r) {
        item.setLista(listaRepository.findById(r.listaId()).orElseThrow(() -> new RecursoNaoEncontradoException("Lista não encontrada: " + r.listaId())));
        item.setProduto(produtoRepository.findById(r.produtoId()).orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + r.produtoId())));
        item.setQuantidade(r.quantidade()); item.setPrecoUnitario(r.precoUnitario()); item.setComprado(r.comprado());
    }
    public void excluir(Long id) { repository.delete(buscar(id)); }
}
