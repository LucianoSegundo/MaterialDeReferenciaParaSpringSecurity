package com.Lsegundo.twinterfalso.entidades;


import com.Lsegundo.twinterfalso.controller.dto.LoginRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

// definindo a classe como entidade e dando nome a tabela

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

	// definindo que essa variavel será usada como id, fazendo
	//com que ela seja gerada automaticamente e definindo um nome para a coluna
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "ususario_id")
	private UUID usuarioId;
	
	// definindo que o valor da variavel a seguir não pode ser repetido.
	
	@Column(unique = true)
	private String nome;
	
	private String senha;
	
	// definindo que o tipo de relação etre a classe e a classe Role é muitos para muitos
	// definindo o nome da tabela em que os ids se encontraram
	// definindo quais os nomes das colunas em que estaram o id do usuário e o id do role.
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "tb_relaçao_usuario_role",
			joinColumns = @JoinColumn(name = "ususario_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			
			)
	private Set<Rolee> rolees;

	public UUID getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(UUID usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Set<Rolee> getRoles() {
		return rolees;
	}

	public void setRoles(Set<Rolee> rolees) {
		this.rolees = rolees;
	}



	public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
		// Compara a senha fornecida com a senha armazenada, usando o PasswordEncoder fornecido
		return passwordEncoder.matches(loginRequest.senha(), this.senha);
	}
}
