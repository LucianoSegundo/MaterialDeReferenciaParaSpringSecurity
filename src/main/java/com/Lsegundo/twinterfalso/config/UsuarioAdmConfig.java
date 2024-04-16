package com.Lsegundo.twinterfalso.config;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.Lsegundo.twinterfalso.entidades.Rolee;
import com.Lsegundo.twinterfalso.entidades.Usuario;
import com.Lsegundo.twinterfalso.repositorio.RoleRepositorio;
import com.Lsegundo.twinterfalso.repositorio.UsuarioRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class UsuarioAdmConfig implements CommandLineRunner {

    private RoleRepositorio Rolerepo;
    private UsuarioRepositorio usuarioRepo;
    private BCryptPasswordEncoder SenhaEncoder;

    // Injeção de dependência dos repositórios e do codificador de senha
    public UsuarioAdmConfig(RoleRepositorio rolerepo, UsuarioRepositorio usuarioRepo, BCryptPasswordEncoder senhaEncoder) {
        Rolerepo = rolerepo;
        this.usuarioRepo = usuarioRepo;
        SenhaEncoder = senhaEncoder;
    }

    // Método para ser executado quando a aplicação é inicializada
    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Busca a role de administrador no repositório
        var roleAdm = Rolerepo.findByNome(Rolee.Values.ADMIN.name());

        // Busca o usuário "admin" no repositório
        var usuarioAdm = usuarioRepo.findByNome("admin");

        // Verifica se o usuário "admin" já existe, se não existir, cria um novo usuário administrador
        usuarioAdm.ifPresentOrElse(
                usuario ->{
                    System.out.println("admin já existe");
                }, () ->{
                    var usuario = new Usuario();
                    usuario.setNome("admin");
                    usuario.setSenha(SenhaEncoder.encode("123"));
                    usuario.setRoles(Set.of(roleAdm));
                    usuarioRepo.save(usuario);
                }
        );

        // Método acima é equivalente a isto
        /*
        if(usuarioAdm.isPresent()){
            System.out.println("admin já existe");
        }
        else{
            var usuario = new Usuario();
            usuario.setNome("admin");
            usuario.setSenha(SenhaEncoder.encode("123"));
            usuario.setRoles(Set.of(roleAdm));
            usuarioRepo.save(usuario);
        }
        */
    }
}
