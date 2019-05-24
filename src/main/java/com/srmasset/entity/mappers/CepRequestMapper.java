package com.srmasset.entity.mappers;

import org.springframework.stereotype.Service;

import com.srmasset.api.dtos.ResponseCepDTO;
import com.srmasset.entity.CepRequest;

@Service
public class CepRequestMapper {
	
	public CepRequest toEntity(ResponseCepDTO dto) {
		CepRequest cep = new CepRequest();
		cep.setCity(dto.getCidade());
		return cep;
	}
	
	public ResponseCepDTO toDto(CepRequest entity) {
		ResponseCepDTO dto = new ResponseCepDTO();
		dto.setCep(entity.getCep());
		dto.setCidade(entity.getCity());
		return dto;
	}
}
