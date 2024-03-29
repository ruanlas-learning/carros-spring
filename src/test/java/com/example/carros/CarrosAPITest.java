package com.example.carros;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.carros.api.carros.Carro;
import com.example.carros.api.carros.CarroDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarrosSpringApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarrosAPITest extends BaseAPITest{

	@Autowired
	protected TestRestTemplate rest;
	
	private ResponseEntity<CarroDTO> getCarro(String url) {
//		return rest.withBasicAuth("user", "123").getForEntity(url, CarroDTO.class);
		return get(url, CarroDTO.class);
	}
	
	private ResponseEntity<List<CarroDTO>> getCarros(String url) {
//		return rest.withBasicAuth("user", "123").exchange(
//				url, 
//				HttpMethod.GET, 
//				null, 
//				new ParameterizedTypeReference<List<CarroDTO>>() {
//				});
		
		HttpHeaders headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<CarroDTO>>() {
                });
	}
	
	@Test
	public void testSave() {
		Carro carro = new Carro();
		carro.setNome("Porshe");
		carro.setTipo("esportivos");
		
//		ResponseEntity response = rest.withBasicAuth("admin", "123").postForEntity("/api/v1/carros", carro, null);
		ResponseEntity response = post("/api/v1/carros", carro, null);
		System.out.println(response);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		String location = response.getHeaders().get("location").get(0);
		CarroDTO c = getCarro(location).getBody();
		
		assertNotNull(c);
		assertEquals("Porshe", c.getNome());
		assertEquals("esportivos", c.getTipo());
		
//		rest.withBasicAuth("admin", "123").delete(location);
		delete(location, null);
		
		assertEquals(HttpStatus.NOT_FOUND, getCarro(location).getStatusCode());
	}
	
	@Test
	public void testLista() {
		List<CarroDTO> carros = getCarros("/api/v1/carros").getBody();
		assertNotNull(carros);
		assertEquals(10, carros.size());
		
		carros = getCarros("/api/v1/carros?page=0&size=30").getBody();
		assertNotNull(carros);
		assertEquals(30, carros.size());
	}
	
	@Test
	public void testListaPorTipo() {
		assertEquals(10, getCarros("/api/v1/carros/tipo/classicos").getBody().size());
		assertEquals(10, getCarros("/api/v1/carros/tipo/esportivos").getBody().size());
		assertEquals(10, getCarros("/api/v1/carros/tipo/luxo").getBody().size());
		
		assertEquals(5, getCarros("/api/v1/carros/tipo/classicos?page=0&size=5").getBody().size());
		assertEquals(5, getCarros("/api/v1/carros/tipo/esportivos?page=0&size=5").getBody().size());
		assertEquals(5, getCarros("/api/v1/carros/tipo/luxo?page=0&size=5").getBody().size());
		
		assertEquals(HttpStatus.NO_CONTENT, getCarros("/api/v1/carros/tipo/xxx").getStatusCode());
	}
	
	@Test
	public void testGetOk() {
		ResponseEntity<CarroDTO> response = getCarro("/api/v1/carros/11");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
		CarroDTO c = response.getBody();
		assertEquals("Ferrari FF", c.getNome());
	}
	
	@Test
	public void testGetNotFound() {
		ResponseEntity<CarroDTO> response = getCarro("/api/v1/carros/1100");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
}
