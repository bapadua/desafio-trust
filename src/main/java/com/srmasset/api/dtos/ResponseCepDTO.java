package com.srmasset.api.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseCepDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cep;
	private String estado;
	private String cidade;
	private String bairro;
	private String logradouro;
}
