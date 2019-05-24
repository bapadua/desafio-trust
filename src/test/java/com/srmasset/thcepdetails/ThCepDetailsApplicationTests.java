package com.srmasset.thcepdetails;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.srmasset.service.TrustChallengeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ThCepDetailsApplicationTests {
	
	private String notValidCepDigit; 
	private String notValidCepLength;
	private String validCep;
	
	@Autowired
	private TrustChallengeService trust;

	@Before
	public void setUp() {
		notValidCepDigit = "06622x64@"; 
		notValidCepLength  = "0662264";
		validCep = "06622640";
	}
	
	
	/**
	 * Cep contém 8 digitos
	 */
	@Test
	public void trueForValidLength() {
		assertTrue(trust.hasValidLength(validCep));
	}
	
	/**
	 * Cep contém apenas numeros
	 */
	public void trueForOnlyNumbers() {
		assertTrue(trust.isCepDigitsOnly(validCep));
	}
	
	/**
	 * Cep inválido se diferente de 8 digitos
	 */
	@Test
	public void falseForInvalidLength() {
		assertFalse(trust.hasValidLength(notValidCepLength));
	}
	
	/**
	 * Cep inválido 
	 */
	@Test
	public void falseForNaN() {
		assertFalse(trust.isCepDigitsOnly(notValidCepDigit));
	}
	
	
	
	
}
