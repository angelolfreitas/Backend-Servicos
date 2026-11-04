package com.elos.negociacao_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
///Decisão 1, identidade: o token só traz o userId, e não existe tabela de clientes aqui.
/// Então, neste serviço, idCliente e idPrestador guardam o id do usuário. Isso diverge do
/// diagrama do banco, anote na documentação.
@SpringBootApplication
public class NegociacaoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NegociacaoServiceApplication.class, args);
	}

}
