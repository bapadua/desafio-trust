package com.srmasset.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.srmasset.api.dtos.ResponseCepDTO;
import com.srmasset.api.response.Response;
import com.srmasset.entity.CepRequest;
import com.srmasset.entity.mappers.CepRequestMapper;
import com.srmasset.repository.CepRepository;

/**
 * Classe que faz acesso ao serviço de consulta de cep da Trust
 * 
 * @author bapad
 *
 */

@Service
public class TrustChallengeService {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	CepRequestMapper mapper;
	
	@Autowired
	CepRepository repository;

	@Value("${api-url}")
	private String apiUrl;

	private static final Logger log = LoggerFactory.getLogger(TrustChallengeService.class);

	/**
	 * Consulta no serviço externo com resultado encapsulado, e usa o ehCache pra guardar a consulta em
	 * memória, configuração fica no arquivo ehcache.xml na pasta resources.
	 *  
	 * @param cep
	 * @return
	 */
	@Cacheable("cepCache")
	public Response<ResponseCepDTO> getCep(String cep) {
		log.info("Chamada no serviço getCep");
		Response<ResponseCepDTO> response = new Response<ResponseCepDTO>();
		List<String> errors = new ArrayList<String>();

		if (!isCepDigitsOnly(cep)) {
			errors.add("O cep deve conter apenas números");
		}
		if (!hasValidLength(cep)) {
			errors.add("O cep deve conter 8 digitos");
		}

		
		ResponseCepDTO result = null;
		
		try {
			log.info("Chamada no serviço externo");
			result = restTemplate.getForObject(apiUrl.concat(cep), ResponseCepDTO.class);
			result.setCep(cep);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			log.info("Application error " + e.getMessage());
		}
		try {
			CepRequest entity = mapper.toEntity(result);
			entity.setCep(cep);
			log.info("Salvando cep pesquisado: " + cep);
			repository.save(entity);	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		response.setData(result);
		response.setErrors(errors);

		return response;

	}
	
	/**
	 * Recebe uma lista de ceps e retorna uma lista de entereços
	 * @param cepList
	 * @return List
	 */
	
	public Response<List<ResponseCepDTO>> getCepList(List<String> cepList) {
		Response<List<ResponseCepDTO>> response = new Response<List<ResponseCepDTO>>();
		List<ResponseCepDTO> list = new ArrayList<ResponseCepDTO>();
		List<String> errors = new ArrayList<>();
		if(cepList.isEmpty()) {
			return null;
		}
		/**
		 * Filtra os ceps repetidos.
		 */
		Set<String> filteredList = new HashSet<String>(cepList);
		filteredList.forEach(cep -> {
			log.info("Consulta no cep" + cep);
			Response<ResponseCepDTO> result = getCep(cep);
			if(result.getData().getEstado() == null) {
				errors.add("CEP não encontrado");
				errors.addAll(result.getErrors());
			} else {
				ResponseCepDTO data = result.getData();
				list.add(data);
			}
		});
		
		response.setData(list);
		response.setErrors(errors);
		
		return response;
	}
	

	public Response<List<ResponseCepDTO>> getAll() {
		Response<List<ResponseCepDTO>> response = new Response<List<ResponseCepDTO>>();
		List<ResponseCepDTO> dtos = new ArrayList<ResponseCepDTO>();
		List<CepRequest> ceps = repository.findAll();
		ceps.forEach(cep ->{
			ResponseCepDTO dto = new ResponseCepDTO();
			dto.setCep(cep.getCep());
			dto.setCidade(cep.getCity());
			dtos.add(dto);
		});
		
		response.setData(dtos);
		
		return response;
	}

	/**
	 * Metodo que recebe o cep e faz algumas validações
	 * 
	 * @param cep
	 * @return Boolean
	 */
	public Boolean isCepDigitsOnly(String cep) {
		String regex = "\\d+";
		return cep.matches(regex);
	}

	/**
	 * Metodo para validar o tamanho do cep
	 * 
	 * @param cep
	 * @return Boolean
	 */
	public Boolean hasValidLength(String cep) {
		return cep.length() == 8;
	}

}
