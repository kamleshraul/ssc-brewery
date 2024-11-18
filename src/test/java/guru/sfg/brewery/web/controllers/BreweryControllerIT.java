package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BreweryControllerIT extends BaseIT {

	
	//following test are  /breweries and /api/v1/breweries for GET with customer role only
			@Test
			void getApiBreweriesHttpBasicsCustomerRole() throws Exception {
				mockMvc.perform(get("/brewery/api/v1/breweries")
					.with(httpBasic("scott", "tiger")))
					.andExpect(status().is2xxSuccessful());
			}
			
			@Test
			void getApiBreweriesHttpBasicsAdminRole() throws Exception {
				mockMvc.perform(get("/brewery/api/v1/breweries")
					.with(httpBasic("spring", "security")))
					.andExpect(status().is2xxSuccessful());
			}
			
			@Test
			void getApiBreweriesHttpBasicsNonCustomerRole() throws Exception {
				mockMvc.perform(get("/brewery/api/v1/breweries")
					.with(httpBasic("user", "password")))
					.andExpect(status().isForbidden());
			}
			
			@Test
			void getApiBreweriesHttpBasicsNonAuthenticatedUser() throws Exception {
				mockMvc.perform(get("/brewery/api/v1/breweries"))
					.andExpect(status().isUnauthorized());
			}
			
			
			
			@Test
			void getBreweriesHttpBasicsCustomerRole() throws Exception {
				mockMvc.perform(get("/brewery/breweries")
					.with(httpBasic("scott", "tiger")))
					.andExpect(status().is2xxSuccessful());
			}
			
			@Test
			void getBreweriesHttpBasicsAdminRole() throws Exception {
				mockMvc.perform(get("/brewery/breweries")
					.with(httpBasic("spring", "security")))
					.andExpect(status().is2xxSuccessful());
			}
			
			@Test
			void getBreweriesHttpBasicsNonCustomerRole() throws Exception {
				mockMvc.perform(get("/brewery/breweries")
					.with(httpBasic("user", "password")))
					.andExpect(status().isForbidden());
			}
			
			@Test
			void getBreweriesHttpBasicsNonAuthenticatedUser() throws Exception {
				mockMvc.perform(get("/brewery/breweries"))
					//.with(httpBasic("user", "password")))
					.andExpect(status().isUnauthorized());
			}
}
