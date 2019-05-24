package com.srmasset.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srmasset.api.dtos.ResponseCepDTO;
import com.srmasset.api.response.Response;
import com.srmasset.service.TrustChallengeService;

import io.swagger.annotations.ApiImplicitParam;
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

	@ApiOperation(value = "Consulta endereço por CEP")
	@ApiImplicitParam(name = "cep", format = "String")
	@GetMapping(value = "/{cep}")
	public ResponseEntity<Response<ResponseCepDTO>> getCepAdress(@PathVariable("cep") String cep) {
		Response<ResponseCepDTO> response = new Response<ResponseCepDTO>();
		log.info("Chamada no endpoint /{cep}");
		try {
			response = trustService.getCep(cep);
		} catch (Exception e) {
			return new ResponseEntity<Response<ResponseCepDTO>>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok().body(response);
	}

	@ApiOperation(value = "Retorna uma lista de endereços a partir de uma lista de ceps	")
	@PostMapping
	public ResponseEntity<Response<List<ResponseCepDTO>>> getAddressList(@RequestBody List<String> list) {
		Response<List<ResponseCepDTO>> response = new Response<List<ResponseCepDTO>>();
		List<String> errors = response.getErrors();
		log.info("Chamada no endpoint de lista /cep");
		try {
			response = trustService.getCepList(list);
		} catch (Exception e) {
			errors.add(e.getMessage());
		}
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation(value = "Retorna todos os ceps pesquisados H2")
	@GetMapping("/all")
	public ResponseEntity<Response<List<ResponseCepDTO>>> getAll() {
		Response<List<ResponseCepDTO>> response = new Response<List<ResponseCepDTO>>();
		List<String> errors = response.getErrors();
		log.info("Chamada no endpoint de lista /all");
		try {
			response = trustService.getAll();
		} catch (Exception e) {
			errors.add(e.getMessage());
		}
		return ResponseEntity.ok().body(response);
	}

}
