package guru.sfg.brewery.bootstrap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

	private final AuthorityRepository authorityRepository;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		if(authorityRepository.count()==0) {
			loadSecurityData();
		}
	}

	
	private void loadSecurityData() {
		
		//beer auth
		Authority createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
		Authority updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
		Authority deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());
		Authority readBeer   = authorityRepository.save(Authority.builder().permission("beer.read").build());
		
		//customer auth
		Authority createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
		Authority updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
		Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());
		Authority readCustomer   = authorityRepository.save(Authority.builder().permission("customer.read").build());
		
		//Brewery
		Authority createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
		Authority updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
		Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());
		Authority readBrewery   = authorityRepository.save(Authority.builder().permission("brewery.read").build());
		
		
		Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
		Role userRole = roleRepository.save(Role.builder().name("USER").build());
		Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
		
		adminRole.setAuthorities(new HashSet(Set.of(
								createBeer,updateBeer,deleteBeer,readBeer
								,createCustomer,readCustomer,deleteCustomer,updateCustomer
								,createBrewery,readBrewery,deleteBrewery,updateBrewery)));
		
		customerRole.setAuthorities(new HashSet(Set.of(readBeer,readCustomer,readBrewery)));
		userRole.setAuthorities(new HashSet(Set.of(readBeer)));

		roleRepository.saveAll(Arrays.asList(adminRole,userRole,customerRole));
		
		userRepository.save(User.builder()
				.username("spring")
				.password(passwordEncoder.encode("security"))
				.role(adminRole)
				.build());
		
		userRepository.save(User.builder()
				.username("user")
				.password(passwordEncoder.encode("password"))
				.role(userRole)
				.build());
		userRepository.save(User.builder()
				.username("scott")
				.password(passwordEncoder.encode("tiger"))
				.role(customerRole)
				.build());
		
		log.debug("Users loaded: "+userRepository.count());
	}
	
	

}
