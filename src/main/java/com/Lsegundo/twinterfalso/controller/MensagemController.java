package com.Lsegundo.twinterfalso.controller;

import com.Lsegundo.twinterfalso.controller.dto.MensagemRequest;
import com.Lsegundo.twinterfalso.entidades.Mensagem;
import com.Lsegundo.twinterfalso.entidades.Rolee;
import com.Lsegundo.twinterfalso.repositorio.MensagemRepositorio;
import com.Lsegundo.twinterfalso.repositorio.UsuarioRepositorio;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class MensagemController {

    private MensagemRepositorio mensagemRepo;
    private UsuarioRepositorio usuarioRepo;

    public MensagemController(MensagemRepositorio mensagemRepo, UsuarioRepositorio usuarioRepo) {
       this.mensagemRepo = mensagemRepo;
       this.usuarioRepo = usuarioRepo;
    }

    @PostMapping("publicarMensagem")
    // metodo usado para receber mensagens do front end
    public ResponseEntity<Void>  publicarMensagem(@RequestBody MensagemRequest mensagemRequest, JwtAuthenticationToken token) throws BadRequestException {

        // conventendo o id presente no token de string para UUID
        var usuario = usuarioRepo.findById( UUID.fromString(token.getName()));

        // criando mensagem, verificando se ela não está vazia e montando a mensagem
        var mensgame = new Mensagem();

        if (mensagemRequest.conteudo().isEmpty()) throw new BadRequestException("mensagem sem conteudo");

        mensgame.setUsuario(usuario.get());
        mensgame.setConteudo(mensagemRequest.conteudo());
        //salvando no BANCO DE DADOS
        mensagemRepo.save(mensgame);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletarMensagem")
    public ResponseEntity<Void> deletarMensagem(@PathVariable("id") Long idMensagem, JwtAuthenticationToken token){

        // consultando se a mensagem existe no banco
        var mensagemNoBanco= mensagemRepo.findById(idMensagem).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        // coletando o id do usuario da mensage,
            var idUsuMensagem = mensagemNoBanco.getUsuario().getUsuarioId();

        // coletando o usuario no banco de dados com base no token
            var usuarioNoBanco = usuarioRepo.findById(UUID.fromString(token.getName()));

        // consultando se o usuario é ou não um adm
            var eAdm = usuarioNoBanco.get()
                    .getRoles().stream()
                    .anyMatch(rolee -> rolee.getNome().equalsIgnoreCase(Rolee.Values.ADMIN.name()));

        // se o usuario for admou o id da mensagem do usuario e o usuario do token forem iguais, apagar mensagem
            if (eAdm|| (idUsuMensagem.equals(UUID.fromString(token.getName())))) mensagemRepo.deleteById(idMensagem);

            else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        //1 hora e 15 minutos;
        return ResponseEntity.ok().build();

    }
}
