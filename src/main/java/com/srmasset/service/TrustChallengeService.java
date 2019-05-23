package com.srmasset.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.srmasset.api.dtos.ResponseCepDTO;
import com.srmasset.api.response.Response;

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
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			log.info("Application error " + e.getMessage());
		}
		response.setData(result);
		response.setErrors(errors);

		return response;

	}
	
	/**
	 * Recebe uma lista de ceps e retorna uma lista de entereços
	 * @param cepList
	 * @return
	 */
	public Response<List<ResponseCepDTO>> getCepList(List<String> cepList) {
		Response<List<ResponseCepDTO>> response = new Response<List<ResponseCepDTO>>();
		List<ResponseCepDTO> list = new ArrayList<ResponseCepDTO>();
		List<String> errors = new ArrayList<String>();	
		if(cepList.isEmpty()) {
			return null;
		}
		cepList.forEach(cep -> {
			ResponseCepDTO result = new ResponseCepDTO();
			Response<ResponseCepDTO> search = getCep(cep);
			
			log.info(search.getData().toString());
		});
		
		response.setData(list);
		response.setErrors(errors);
		
		return response;
	}

	/**
	 * Metodo que recebe o cep e faz algumas validações
	 * 
	 * @param cep
	 * @return
	 */
	public Boolean isCepDigitsOnly(String cep) {
		String regex = "\\d+";
		return cep.matches(regex);
	}

	/**
	 * Metodo para validar o tamanho do cep
	 * 
	 * @param cep
	 * @return
	 */
	public Boolean hasValidLength(String cep) {
		return cep.length() == 8;
	}

}
