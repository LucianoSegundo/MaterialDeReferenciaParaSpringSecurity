package com.Lsegundo.twinterfalso.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Injeta a chave pública JWT do arquivo de propriedades
    @Value("${jwt.public.key}")
    private RSAPublicKey chavePublica;

    // Injeta a chave privada JWT do arquivo de propriedades
    @Value("${jwt.private.key}")
    private RSAPrivateKey chavePrivada;

    // Configuração do filtro de segurança para as requisições HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                // Configura as autorizações para as requisições HTTP
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/cadastro").permitAll()

                        .anyRequest().authenticated())
                // Desabilita a proteção contra CSRF
                .csrf(csrf -> csrf.disable())
                // Configura o servidor de recursos OAuth 2.0 para usar JWT
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // Configura a política de gerenciamento de sessões para STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    // Configuração do decodificador JWT
    @Bean
    public JwtDecoder decodificadorJwt() {
        return NimbusJwtDecoder.withPublicKey(chavePublica).build();
    }

    // Configuração do codificador JWT
    @Bean
    public JwtEncoder codificadorJwt() {
        // Cria um objeto JWK com as chaves pública e privada
        JWK jwk = new RSAKey.Builder(this.chavePublica)
                .privateKey(chavePrivada)
                .build();
        // Cria um conjunto de JWKs imutável a partir do JWK criado
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        // Retorna um codificador NimbusJwtEncoder com o conjunto de JWKs
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder senhaEncoder() {
        return new BCryptPasswordEncoder();
    }
}
// comando do terminal para gerar chave privada "openssl genrsa > app.key"
// app deve ser substituido pelo nome do arquivo em que a chave deve ser salva.
// comando para escrever chave publica "openssl rsa -in app.key -pubout -out app.pub"
// app.key é o arquivo em que a chave publica se baseara, app.pub é o arquivo em que
// a chave publica será criada.
