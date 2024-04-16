package com.Lsegundo.twinterfalso.controller;

import com.Lsegundo.twinterfalso.controller.dto.CadastroRequest;
import com.Lsegundo.twinterfalso.entidades.Rolee;
import com.Lsegundo.twinterfalso.entidades.Usuario;
import com.Lsegundo.twinterfalso.repositorio.RoleRepositorio;
import com.Lsegundo.twinterfalso.repositorio.UsuarioRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UsuarioController {

    private final UsuarioRepositorio usuarioRepo;
    private final RoleRepositorio roleRepo;
    private final BCryptPasswordEncoder codificadorSenha;

    public UsuarioController(UsuarioRepositorio usuarioRepo,
                             RoleRepositorio roleRepo,
                             BCryptPasswordEncoder codificadorSenha) {

        this.usuarioRepo = usuarioRepo;
        this.roleRepo = roleRepo;
        this.codificadorSenha = codificadorSenha;
    }

    // Endpoint para cadastrar usuários
    @PostMapping("/cadastro")
    @Transactional
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody CadastroRequest CadRequest) {

        //coletando o role do banco de dados;
        var basicRole = roleRepo.findByNome(Rolee.Values.BASIC.name());

        // realizando uma consulta no banco de dados e conferindo se o usuario já existe
        var usuarioNoBonco = usuarioRepo.findByNome(CadRequest.nome());

        if (usuarioNoBonco.isPresent()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        // montando usuario
        var novoUsuario = new Usuario();

        novoUsuario.setNome(CadRequest.nome());
        novoUsuario.setRoles(Set.of(basicRole));
        novoUsuario.setSenha(codificadorSenha.encode(CadRequest.senha()));

        // salvando no banco de dados;
        usuarioRepo.save(novoUsuario);

        return ResponseEntity.ok().build();
    }


    @GetMapping("listarUsuarios")
    // o nome do scope deve estar igual com o banco de dados, no
    // caso admin esta em minusculo pois no banco está em minusculo.
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<Usuario>> listarUsuarios() {

        var usuarios = usuarioRepo.findAll();
        return ResponseEntity.ok(usuarios);
    }
}
