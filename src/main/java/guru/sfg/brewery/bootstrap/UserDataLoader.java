package guru.sfg.brewery.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

	private final AuthorityRepository authorityRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		if(authorityRepository.count()==0) {
			loadSecurityData();
		}
	}

	private void loadSecurityData() {
		Authority admin = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
		Authority userRole = authorityRepository.save(Authority.builder().role("ROLE_USER").build());
		Authority customer = authorityRepository.save(Authority.builder().role("ROLE_CUSTOMER").build());
		
		userRepository.save(User.builder()
				.username("spring")
				.password(passwordEncoder.encode("security"))
				.authority(admin)
				.build());
		userRepository.save(User.builder()
				.username("user")
				.password(passwordEncoder.encode("password"))
				.authority(userRole)
				.build());
		userRepository.save(User.builder()
				.username("scott")
				.password(passwordEncoder.encode("tiger"))
				.authority(customer)
				.build());
	}
	
	

}