package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlParameterAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*If we define password encoder then it will override default encoder and use this one*/
	/*@Bean
	PasswordEncoder passwordEncoder() {
		//NoOpPassword encoder is used for legacy system, it does not calculate hash, it keep password as plain text
		return NoOpPasswordEncoder.getInstance();
	}*/
	
	
	protected RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManger) {
		RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
		filter.setAuthenticationManager(authenticationManger);
		return filter;
	}
	
	protected RestUrlParameterAuthFilter restUrlParameterAuthFilter(AuthenticationManager authenticationManger) {
		RestUrlParameterAuthFilter filter = new RestUrlParameterAuthFilter(new AntPathRequestMatcher("/api/**"));
		filter.setAuthenticationManager(authenticationManger);
		return filter;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
			
		
			/*Add our custom Authentication Filter before UsernamePasswordAuthenticationFilter
			 * */
			http.addFilterBefore(restHeaderAuthFilter(authenticationManager())
								,UsernamePasswordAuthenticationFilter.class)
				.csrf().disable(); //disable csrf for testing purpose here
			
			/*Add our custom Authentication Filter before RestHeaderAuthFilter*/
			http.addFilterBefore(restUrlParameterAuthFilter(authenticationManager())
							,RestHeaderAuthFilter.class)
			.csrf().disable(); // no need to define here as it is global setting once is suffice
		
			http
                .authorizeRequests(authorize ->
                        authorize
                        	.antMatchers("/h2-console/**").permitAll() // use to access h2 console
                        	.antMatchers("/","/webjars/**","/resources/**").permitAll()
                        	.antMatchers("/beers/find/**","/beer*").permitAll()
                        	.antMatchers(HttpMethod.GET,"/api/v1/beer/**").permitAll()
                        	.mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/{upc}").permitAll()
                )
                .authorizeRequests(requests -> requests
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
			
			//h2 console setting
			http.headers().frameOptions().sameOrigin();
			
	}

	
	/*
	 * @Bean PasswordEncoder passwordEncoder() { return new
	 * LdapShaPasswordEncoder(); }
	 */
	
	/*
	 * @Bean PasswordEncoder passwordEncoder() { return new
	 * StandardPasswordEncoder(); //sha256 ecoder }
	 */
	
	/*
	 * @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();
	 * }
	 */
	
	/*
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}*/
	
	@Bean //using our own implementation of PasswordEncoderFactory
	PasswordEncoder passwordEncoder() {
		return CustomPasswordEncoderFactory.createDelegatingPasswordEncoder();
	}
	
	// another way to create InMemoryUserDetails
	
/* Commenting as we have created Our own JpaUserDetailsService to load username and password from db
 * 
 * 	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("spring")
			.password("{bcrypt}$2a$10$Ske5O1y8gwkluixPGnlg0.rDsMtVSqmkIDLBc0oZYPqfX2vz9RfXK") //{noop} is required as we not configuring any password encoder
			.roles("ADMIN")
			.and()
			.withUser("user")
			//.password("{SSHA}nWpQ8r6ofjVVKEIVMedRcdk9GprTkFCKS/YSQA==")
			.password("{bcrypt}$2a$10$uelHC8xqeuONaAWHNH8/8.AeixMpq8KJ5/X/ZcstlYssg3C6bdK/u")
			.roles("User")
			.and()
			.withUser("scott")
			//.password("{noop}tiger") // we defined NoOpPassword encoder due to which {noop} not needed any more
			.password("{bcrypt15}$2a$15$KsT.tgU7KoIJ9GaIy0tJQO1oLC6.H0ouXCd19ToqK5jxj4JQ/VetW") 
			//make sure you add {bcrypt15} in password for spring security to detect our custom password encoder
			.roles("Customer");
			
	}
*/
	//creating our own inmemory userdetailservice for credentials
	//as we implemented our own , user credentials defined in application.properties
	//won't get used now
	/*@Override
	@Bean   
	protected UserDetailsService userDetailsService() {
		UserDetails admin = User.withDefaultPasswordEncoder()
				.username("spring")
				.password("security")
				.roles("ADMIN")
				.build();
		
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();
		
		return new InMemoryUserDetailsManager(admin,user);
		
		
	}
	*/
	
	

}
