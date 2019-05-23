package com.srmasset.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	private final static String apiUrl = "https://zuul.trusthub.com.br/orchestrator/v1/obter-endereco-por-cep/";

	private static final Logger log = LoggerFactory.getLogger(TrustChallengeService.class);

	/**
	 * Consulta no serviço externo com resultado encapsulado.
	 * 
	 * @param cep
	 * @return
	 */
	public Response<ResponseCepDTO> getCep(String cep) {
		Response<ResponseCepDTO> response = new Response<ResponseCepDTO>();
		if (cep.length() < 8) {
			response.getErrors().add("O cep deve conter 8 caracteres");
		}
		log.info("Call to the getCepService");
		ResponseCepDTO result = null;
		try {
			result = restTemplate.getForObject(apiUrl.concat(cep), ResponseCepDTO.class);
			log.info(result.toString());
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			log.info("Application error " + e.getMessage());
		}
		response.setData(result);

		return response;
	}

	/**
	 * Metodo que recebe o cep e faz algumas validações
	 * 
	 * @param cep
	 * @return
	 */
	public static Boolean isCepDigitsOnly(String cep) {
		String regex = "\\d+";
		return cep.matches(regex);
	}
	
	public static Boolean hasValidLength(String cep) {
		return cep.length() == 8;
	}

}
