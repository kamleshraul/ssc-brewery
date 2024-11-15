package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize ->
                        authorize
                        	.antMatchers("/","/webjars/**","/resources/**").permitAll()
                        	.antMatchers("/beers/find/**","/beer*").permitAll()
                        	.antMatchers(HttpMethod.GET,"/api/v1/beer/**").permitAll()
                        	.mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/{upc}").permitAll()
                )
                .authorizeRequests(requests -> requests
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
			
	}

	// another way to create InMemoryUserDetails
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("spring")
			.password("{noop}security") //{noop} is required as we not configuring any password encoder
			.roles("ADMIN")
			.and()
			.withUser("user")
			.password("{noop}password")
			.roles("User")
			.and()
			.withUser("scott")
			.password("{noop}tiger")
			.roles("Customer");
			
	}

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
