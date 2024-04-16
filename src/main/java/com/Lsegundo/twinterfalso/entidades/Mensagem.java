package com.Lsegundo.twinterfalso.entidades;

import jakarta.persistence.*;
import com.Lsegundo.twinterfalso.entidades.Usuario;
//definindo a classe como entidade e dando nome a tabela

@Entity
@Table(name = "tb_mensagens")
public class Mensagem {
		
// definindo que essa variavel será usada como id, fazendo
//com que ela seja gerada automaticamente e definindo um nome para a coluna
			
	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE)
	@Column(name = "mensagem_id")
	private Long mensagemId;

	@ManyToOne
	@JoinColumn(name = "ususario_id")
	private Usuario usuario;
	
	private String conteudo;

	public long getMensagemId() {
		return mensagemId;
	}

	public void setMensagemId(Long mensagemId) {
		this.mensagemId = mensagemId;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
