package ru.romanow.inst.services.common.config

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.romanow.inst.services.common.properties.ActuatorSecurityProperties

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Order(SECOND)
    @Configuration
    class JwtResourceProtectionConfiguration : WebSecurityConfigurerAdapter() {
        override fun configure(http: HttpSecurity) {
            http.antMatcher("/api/v1/**")
                .authorizeHttpRequests {
                    it.anyRequest().authenticated()
                }
                .oauth2ResourceServer {
                    it.jwt()
                }
        }
    }

    @Order(LAST)
    @Configuration
    class ManagementSecurityConfiguration(
        private val actuatorSecurityProperties: ActuatorSecurityProperties
    ) : WebSecurityConfigurerAdapter() {

        @Bean
        fun passwordEncoder(): PasswordEncoder {
            return BCryptPasswordEncoder()
        }

        override fun configure(http: HttpSecurity) {
            // @formatter:off
            http.requestMatcher(EndpointRequest.toAnyEndpoint().excluding(HealthEndpoint::class.java))
                .authorizeRequests()
                .anyRequest()
                .hasRole(actuatorSecurityProperties.role)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
            // @formatter:on
        }

        override fun configure(auth: AuthenticationManagerBuilder) {
            val passwordEncoder: PasswordEncoder = passwordEncoder()
            auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser(actuatorSecurityProperties.user)
                .password(passwordEncoder.encode(actuatorSecurityProperties.passwd))
                .roles(actuatorSecurityProperties.role)
        }
    }
}