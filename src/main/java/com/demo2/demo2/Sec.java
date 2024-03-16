package com.demo2.demo2;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
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
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
//	UserDetailsService userd(DataSource ds) {
//		var u1=User.withUsername("deep").password("dpk").roles("ADMIN").passwordEncoder(str->pe().encode(str)).build();
//		
//		return new InMemoryUserDetailsManager(u1);
//	}
//	
//	
//	

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()	
        	//.url("jdbc:postgresql://dpg-cnq8rkq1hbls738jng7g-a.oregon-postgres.render.com:5432/database_oa07")
          .url("jdbc:postgresql://dpg-cnq8rkq1hbls738jng7g-a/database_oa07")
        	.username("database_oa07_user")
            .password("cN5KILm1sR2qlV4pLAgLgOrNDZZ5ihnv")
            .driverClassName("org.postgresql.Driver")
            .build();
        		
//        		.url("jdbc:postgresql://localhost:5432/my_database")
//        
//                	.username("postgres")
//                    .password("Deepak@1")
//                    .driverClassName("org.postgresql.Driver")
//                    .build();
    }

    
    
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("red-cnqiq56n7f5s7387nvfg");
        config.setPort(6379);
//        config.setUsername("red-cnqiq56n7f5s7387nvfg");
//        config.setPassword("q7hnW6QCJcHmUVG3q8os3rnc25bR9mxy");
        // You can customize other properties of the connection configuration here

        return new LettuceConnectionFactory(config);
    }
    
    @Primary
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
      redisTemplate.setKeySerializer(new StringRedisSerializer());
        // Add other serializers if needed for the value
        return redisTemplate;
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(pe());
        // You may need to set a PasswordEncoder if your UserDetailsService doesn't specify one
        // provider.setPasswordEncoder(passwordEncoder());
        return provider;
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
