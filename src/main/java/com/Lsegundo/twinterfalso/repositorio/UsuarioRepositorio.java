package com.Lsegundo.twinterfalso.repositorio;

import java.util.Optional;
import java.util.UUID;

import com.Lsegundo.twinterfalso.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByNome(String nome);
}
