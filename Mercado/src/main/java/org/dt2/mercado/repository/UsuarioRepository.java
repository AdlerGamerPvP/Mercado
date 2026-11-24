package org.dt2.mercado.repository;
import org.dt2.mercado.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { }
