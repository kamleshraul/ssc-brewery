package guru.sfg.brewery.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;

	@Transactional // it is needed otherwise line 38 will give lazy initialization exceptions
	// as transaction is closed by SpringDataJPA after line 36 statement is completed
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.debug("Getting User	info via JPA");
		
		User user=userRepository.findByUsername(username)
					.orElseThrow(()-> new UsernameNotFoundException("Username "+username+" Not Found"));
		
		return new org.springframework.security.core.userdetails
					.User(user.getUsername()
							, user.getPassword()
							,user.getEnabled()
							,user.getAccountNonExpired()
							,user.getCredentialsNonExpired()
							,user.getAccountNonLocked()
							,convertToSpringAuthorities(user.getAuthorities()));
	}

	private Collection<? extends GrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {
		
		if(authorities!=null && authorities.size()>0) {
			return authorities.stream()
						.map(Authority::getRole)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toSet());
		}
		
		return new HashSet<>();
	}
	
	
}
