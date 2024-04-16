package com.Lsegundo.twinterfalso.repositorio;

import java.util.UUID;

import com.Lsegundo.twinterfalso.entidades.Rolee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepositorio extends JpaRepository<Rolee, Long> {

    Rolee findByNome(String name);
}
