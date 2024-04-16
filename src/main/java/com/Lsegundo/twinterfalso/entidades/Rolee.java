package com.Lsegundo.twinterfalso.entidades;

import jakarta.persistence.*;

//definindo a classe como entidade e dando nome a tabela

@Entity
@Table(name = "tb_roles")
public class Rolee {
	 
// definindo que essa variavel será usada como id, fazendo
//com que ela seja gerada automaticamente e definindo um nome para a coluna
			
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "role_id")
private Long roleId;

private String nome;

//criando um enum para separar os tipos de usuario
public enum Values{
	
	ADMIN(1L),
	
	BASIC(2L);
	
	Long roleid;
	
	private Values(Long roleid) {
		this.roleid = roleid;
	}

	public Long getRoleid() {
		return roleid;
	}
	
}

public long getRoleId() {
	return roleId;
}

public void setRoleId(Long roleId) {
	this.roleId = roleId;
}

public String getNome() {
	return nome;
}

public void setNome(String nome) {
	this.nome = nome;
}

}
