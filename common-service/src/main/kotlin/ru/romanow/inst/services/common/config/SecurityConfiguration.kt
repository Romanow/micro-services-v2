package ru.romanow.inst.services.common.config

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.toAnyEndpoint
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import ru.romanow.inst.services.common.properties.ActuatorSecurityProperties

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val properties: ActuatorSecurityProperties,
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun tokenSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.antMatcher("/api/v1/**")
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .oauth2ResourceServer {
                it.jwt()
            }
            .build()
    }

    @Bean
    fun managementSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // @formatter:off
        http.requestMatcher(toAnyEndpoint().excluding(HealthEndpoint::class.java))
                .authorizeRequests().anyRequest().hasRole(properties.role)
            .and()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .httpBasic()
        // @formatter:on

        return http.build()
    }

    @Bean
    fun users(passwordEncoder: PasswordEncoder): UserDetailsService {
        val user = User.builder()
            .username(properties.user)
            .password(passwordEncoder.encode(properties.passwd))
            .roles(properties.role)
            .build()
        return InMemoryUserDetailsManager(user)
    }
}