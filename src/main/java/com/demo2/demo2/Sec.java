package com.demo2.demo2;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collections;
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
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.CacheControlConfig;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.CacheControl;

@Configuration
public class Sec {


	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
			   .requestMatchers("/signup").permitAll()
			   .requestMatchers("/deteleall","/delete-all/{zone}","/delete","/add","/add-all","/zone/{oldz}/{newz}","/users","/user/{name}","/user").hasAnyRole("ADMIN")
			   .anyRequest().authenticated()
			    );
		http.httpBasic();
		http.headers(s->s.cacheControl(c->c.disable()));
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
//        	.url("jdbc:postgresql://dpg-cnq8rkq1hbls738jng7g-a.oregon-postgres.render.com:5432/database_oa07") //dev
          .url("jdbc:postgresql://dpg-cnq8rkq1hbls738jng7g-a/database_oa07") //production
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
        config.setHostName("red-cnqiq56n7f5s7387nvfg"); //production
        config.setPort(6379); //production   
//        config.setHostName("oregon-redis.render.com"); //dev
//        config.setPort(6379); //dev
//        config.setUsername("red-cnqiq56n7f5s7387nvfg"); //dev
//        config.setPassword("q7hnW6QCJcHmUVG3q8os3rnc25bR9mxy");//dev
//        
//        config.setHostName("localhost"); //loc
//        config.setPort(6379); //loc
//        
        return new LettuceConnectionFactory(config);
    }
    


   
    @Bean
    public RedisTemplate<String, List<Laws>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<Laws>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new ListLawsRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private static class ListLawsRedisSerializer implements RedisSerializer<List<Laws>> {

        private final ObjectMapper objectMapper;

        public ListLawsRedisSerializer() {
            this.objectMapper = new ObjectMapper();
        }

        @Override
        public byte[] serialize(List<Laws> lawsList) throws SerializationException {
            try {
                return objectMapper.writeValueAsBytes(lawsList);
            } catch (Exception ex) {
                throw new SerializationException("Error serializing object", ex);
            }
        }

        @Override
        public List<Laws> deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length == 0) { // Check if the byte array is empty
                return Collections.emptyList(); // Return an empty list
            }
            try {
                return objectMapper.readValue(bytes, new TypeReference<List<Laws>>() {});
            } catch (Exception ex) {
                throw new SerializationException("Error deserializing object", ex);
            }
        }
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(pe());
        return provider;
    }
    

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
    	JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
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
