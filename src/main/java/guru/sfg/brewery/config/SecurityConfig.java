package guru.sfg.brewery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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

}
