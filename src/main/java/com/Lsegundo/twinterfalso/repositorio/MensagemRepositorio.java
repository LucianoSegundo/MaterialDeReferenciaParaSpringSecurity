package com.Lsegundo.twinterfalso.repositorio;

import java.util.UUID;

import com.Lsegundo.twinterfalso.entidades.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MensagemRepositorio extends JpaRepository<Mensagem, Long> {

}
