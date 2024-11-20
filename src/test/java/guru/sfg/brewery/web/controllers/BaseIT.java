package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;

abstract public class BaseIT {

	@Autowired
	WebApplicationContext wac;
	
	public MockMvc mockMvc;
	
	/* commented because now be wan't to connect to real H2 database
	@MockBean
	BeerRepository beerRepository;
	
	@MockBean
	BeerInventoryRepository beerInventoryRepository;
	
	@MockBean
	BreweryService breweryService;
	
	@MockBean
	CustomerRepository customerRepository;
	
	@MockBean
	BeerService beerService;
	*/
	
	@BeforeEach
	public void setUp() {
		mockMvc=MockMvcBuilders
				.webAppContextSetup(wac)
				.apply(springSecurity())
				.build();
	}
	
	public static Stream<Arguments> getStreamAllUsers() {
        return Stream.of(Arguments.of("spring" , "security"),
                Arguments.of("scott", "tiger"),
                Arguments.of("user", "password"));
    }

    public static Stream<Arguments> getStreamNotAdmin() {
        return Stream.of(Arguments.of("scott", "tiger"),
                Arguments.of("user", "password"));
    }

    public static Stream<Arguments> getStreamAdminCustomer() {
        return Stream.of(Arguments.of("spring" , "security"),
                Arguments.of("scott", "tiger"));
    }
}
