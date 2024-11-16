package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;

@WebMvcTest
public class BeerControllerIT extends BaseIT {
	
	
	/*deleteBeer and deleteBeerBadCredentials test RestUrlParameterAuthFilter*/
	@Test
	void deleteBeerUrlParamter() throws Exception{
		mockMvc.perform(delete("/api/v1/beer/493410b3-dd0b-4b78-97bf-289f50f6e74f")
				.param("Api-Key", "spring")
				.param("Api-Secret", "security"))
				.andExpect(status().isOk());
	}
	
	@Test
	void deleteBeerUrlParameterBadCredentials() throws Exception{
		mockMvc.perform(delete("/api/v1/beer/493410b3-dd0b-4b78-97bf-289f50f6e74f")
				.param("Api-Key", "spring")
				.param("Api-Secret", "securityXXX"))
				.andExpect(status().isUnauthorized());
	}
	

	/*deleteBeer and deleteBeerBadCredentials test RestHeaderAuthFilter*/
	@Test
	void deleteBeer() throws Exception{
		mockMvc.perform(delete("/api/v1/beer/493410b3-dd0b-4b78-97bf-289f50f6e74f")
				.header("Api-Key", "spring")
				.header("Api-Secret", "security"))
				.andExpect(status().isOk());
	}
	
	@Test
	void deleteBeerBadCredentials() throws Exception{
		mockMvc.perform(delete("/api/v1/beer/493410b3-dd0b-4b78-97bf-289f50f6e74f")
				.header("Api-Key", "spring")
				.header("Api-Secret", "securityXXX"))
				.andExpect(status().isUnauthorized());
	}
	

	@Test
	void initCreationFormBcrypt15() throws Exception{
		mockMvc.perform(get("/beers/new").with(httpBasic("scott", "tiger")))
			.andExpect(status().isOk())
			.andExpect(view().name("beers/createBeer"))
			.andExpect(model().attributeExists("beer"));
	}
	
	@Test
	void initCreationForm() throws Exception{
		mockMvc.perform(get("/beers/new").with(httpBasic("user", "password")))
			.andExpect(status().isOk())
			.andExpect(view().name("beers/createBeer"))
			.andExpect(model().attributeExists("beer"));
	}
	
	@WithMockUser("spring")
	@Test
	void findBeers() throws Exception{
		mockMvc.perform(get("/beers/find"))
			.andExpect(status().isOk())
			.andExpect(view().name("beers/findBeers"))
			.andExpect(model().attributeExists("beer"));
	}

	@Test
	void findBeersHttpBasic() throws Exception{
		mockMvc.perform(get("/beers/find").with(httpBasic("spring", "security")))
			.andExpect(status().isOk())
			.andExpect(view().name("beers/findBeers"))
			.andExpect(model().attributeExists("beer"));
	}
	
	@Test
	void findBeersBySkippingHttpBasicAuth() throws Exception{
		mockMvc.perform(get("/beers/find"))
			.andExpect(status().isOk())
			.andExpect(view().name("beers/findBeers"))
			.andExpect(model().attributeExists("beer"));
	}
	
	@Test
	void findBeersBySkippingHttpBasicAuthAnonymous() throws Exception{
		mockMvc.perform(get("/beers/find").with(anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("beers/findBeers"))
			.andExpect(model().attributeExists("beer"));
	}
}
