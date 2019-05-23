package com.srmasset.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srmasset.api.dtos.ResponseCepDTO;
import com.srmasset.api.response.Response;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author bapad
 *
 */
@RestController
@RequestMapping("/cep")
public class TheCepApi {

	@Autowired
	TrustChallengeService trustService;

	private static final Logger log = LoggerFactory.getLogger(TheCepApi.class);

	@ApiOperation(value = "Consulta por CEP")
	@GetMapping(value = "/{cep}")
	public ResponseEntity<Response<ResponseCepDTO>> getCepAdress(@PathVariable("cep") String cep) {
		Response<ResponseCepDTO> response = new Response<ResponseCepDTO>();
		log.info("Chamada no endpoint /{cep}");
		try {
			response = trustService.getCep(cep);
		} catch (Exception e) {
			return new ResponseEntity<Response<ResponseCepDTO>>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Response<List<ResponseCepDTO>>> getAddressList(@RequestBody List<String> list){
		return null;
	}
}