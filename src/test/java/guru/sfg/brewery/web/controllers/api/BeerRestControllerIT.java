package guru.sfg.brewery.web.controllers.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;

//@WebMvcTest
@SpringBootTest // added to use JPArepository
class BeerRestControllerIT extends BaseIT {
	
	
	@Autowired
    BeerRepository beerRepository;

    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests {

        public Beer beerToDelete() {
            Random rand = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Delete Me Beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99999999)))
                    .build());
        }

        @Test
        void deleteBeerHttpBasic() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("spring", "security")))
                    .andExpect(status().is2xxSuccessful());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void deleteBeerHttpBasicNotAuth(String user, String pwd) throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("List Beers")
    @Nested
    class ListBeers {
        @Test
        void findBeers() throws Exception {
            mockMvc.perform(get("/api/v1/beer/"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeersAUTH(String user, String pwd) throws Exception {
            mockMvc.perform(get("/api/v1/beer/").with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("Get Beer By ID")
    @Nested
    class GetBeerById {
        @Test
        void findBeerById() throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeerByIdAUTH(String user, String pwd) throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Find By UPC")
    class FindByUPC {
        @Test
        void findBeerByUpc() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeerByUpcAUTH(String user, String pwd) throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }
    }
	
	
	/*	// following deleteBeerHttpBasics check for role for delete method and /api/v1/beer url
	/*	@Test
		void deleteBeerHttpBasicsAdminRole() throws Exception {
			mockMvc.perform(delete("/api/v1/beer/493410b3-dd0b-4b78-97bf-289f50f6e74f")
					.with(httpBasic("spring", "security")))
					.andExpect(status().is2xxSuccessful());
		}
		
		@Test
		void deleteBeerHttpBasicsUserRole() throws Exception {
			mockMvc.perform(delete("/api/v1/beer/493410b3-dd0b-4b78-97bf-289f50f6e74f")
					.with(httpBasic("user", "password")))
					.andExpect(status().isForbidden());
		}
		
		@Test
		void deleteBeerHttpBasicsCustomerRole() throws Exception {
			mockMvc.perform(delete("/api/v1/beer/493410b3-dd0b-4b78-97bf-289f50f6e74f")
					.with(httpBasic("scott", "tiger")))
					.andExpect(status().isForbidden());
		}
		
	*/	
		/*deleteBeer and deleteBeerBadCredentials test RestUrlParameterAuthFilter*/
	/*	@Test
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
	*/	

		/*deleteBeer and deleteBeerBadCredentials test RestHeaderAuthFilter*/
	/*	@Test
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
	void findBeers() throws Exception {
		mockMvc.perform(get("/api/v1/beer/"))
			.andExpect(status().isOk());
	}
	
	@Test
	void findBeerById() throws Exception{
		mockMvc.perform(get("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311"))
				.andExpect(status().isOk());
	}
	
	@Test
	void findBeerByUPC() throws Exception{
		mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
				.andExpect(status().isOk());
	}
*/
}
