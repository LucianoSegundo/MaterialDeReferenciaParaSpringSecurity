package com.Lsegundo.twinterfalso.controller;

import com.Lsegundo.twinterfalso.controller.dto.LoginRequest;
import com.Lsegundo.twinterfalso.controller.dto.LoginResponse;
import com.Lsegundo.twinterfalso.entidades.Rolee;
import com.Lsegundo.twinterfalso.repositorio.UsuarioRepositorio;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {
    private final JwtEncoder codificadorJwt; // Encoder JWT para gerar tokens
    private final UsuarioRepositorio usuarioRepositorio; // Repositório para acessar dados do usuário
    private BCryptPasswordEncoder codificadorSenha; // Encoder para senhas BCrypt

    // Construtor que injeta as dependências necessárias
    public TokenController(JwtEncoder codificadorJwt,
                           UsuarioRepositorio usuarioRepositorio,
                           BCryptPasswordEncoder codificadorSenha) {

        this.usuarioRepositorio = usuarioRepositorio;
        this.codificadorJwt = codificadorJwt;
        this.codificadorSenha = codificadorSenha;
    }

    // Endpoint para autenticação de usuários
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUsuario(@RequestBody LoginRequest loginRequest) {

        // Procura o usuário com base no nome fornecido na solicitação
        var usuario = usuarioRepositorio.findByNome(loginRequest.nome());

        // Verifica se o usuário não foi encontrado ou se as credenciais não correspondem
        if (usuario.isEmpty() || !usuario.get().isLoginCorrect(loginRequest, codificadorSenha)) {
            // Lança uma exceção indicando credenciais inválidas
            throw new BadCredentialsException("Usuario ou senha invalidos");
        }

        // Cria um instante representando o momento atual
        var now = Instant.now();

        // Define o tempo de expiração do token (em segundos)
        var expiresIn = 300L;

        // serve para identificar qual o tipo de usuario com base no token
        var scopes = usuario.get().getRoles()
                .stream()
                .map(Rolee::getNome)
                .collect(Collectors.joining(" "));

        // Constrói as reivindicações (claims) do token JWT
        var claims = JwtClaimsSet.builder()
                .issuer("meu backend")
                .subject(usuario.get().getUsuarioId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        // Codifica o token JWT com base nas reivindicações especificadas
        var jwtValue = codificadorJwt.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        // Retorna uma resposta HTTP com o token JWT e o tempo de expiração
        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
// 51 minutos de video;