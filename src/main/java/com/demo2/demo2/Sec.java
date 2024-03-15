package com.demo2.demo2;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Sec {


	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
			   .requestMatchers("/signup").permitAll()
			   .anyRequest().authenticated()
			    );
		http.httpBasic(withDefaults());
		http.csrf().disable();
		http.sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.headers().frameOptions().sameOrigin();
		return http.build();
	}
	
	@Bean
	WebMvcConfigurer corsf() {
		return new WebMvcConfigurer() {
			 public void addCorsMappings(CorsRegistry registry) {
				 registry.addMapping("/**")
				 .allowedMethods("*")
				 .allowedOrigins("*");
			}
		};
	}
//	
//	@Bean
//	DataSource ds() {
//	return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION).build();
//		
//	}
//	
//	@Bean
//	UserDetailsService userd(DataSource ds) {
//		var u1=User.withUsername("deep").password("dpk").roles("ADMIN").passwordEncoder(str->pe().encode(str)).build();
//		JdbcUserDetailsManager j=new JdbcUserDetailsManager(ds);
//		j.createUser(u1);
//		return j;
//	}
//	
//	
	

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .url("jdbc:postgresql://dpg-cnq8rkq1hbls738jng7g-a/database_oa07")
            .username("database_oa07_user")
            .password("cN5KILm1sR2qlV4pLAgLgOrNDZZ5ihnv")
            .driverClassName("org.postgresql.Driver")
            .build();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        return userDetailsManager;
    }

    @Bean
    public UserDetailsService userDetailsService(JdbcUserDetailsManager userDetailsManager) {
        if (!userDetailsManager.userExists("deep")) {
            userDetailsManager.createUser(
                User.withUsername("deep")
                    .password("dpk")
                    .passwordEncoder(str->pe().encode(str))
                    .roles("ADMIN")
                    .build()
            );
        }
        return userDetailsManager;
    }

	
	@Bean
	BCryptPasswordEncoder pe() {
		return new BCryptPasswordEncoder();
	}
	 @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }

}
