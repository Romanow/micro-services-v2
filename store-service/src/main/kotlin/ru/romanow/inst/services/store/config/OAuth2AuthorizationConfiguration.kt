package ru.romanow.inst.services.store.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import ru.romanow.inst.services.common.config.FIRST
import ru.romanow.inst.services.common.config.SECOND

@Configuration
@EnableWebSecurity
class OAuth2AuthorizationConfiguration {

    @Order(FIRST)
    @Configuration
    class OAuth2LoginConfiguration : WebSecurityConfigurerAdapter() {

        override fun configure(http: HttpSecurity) {
            http.antMatcher("/api/v1/store/**")
                .authorizeHttpRequests {
                    it.anyRequest().authenticated()
                }
                .oauth2ResourceServer {
                    it.jwt()
                }
        }
    }

    @Order(SECOND)
    @Configuration
    class JwtResourceProtectionConfiguration : WebSecurityConfigurerAdapter() {
        override fun configure(http: HttpSecurity) {
            http.requestMatchers { it.antMatchers("/api/v1/authorization", "/oauth2/**", "/login/**") }
                .authorizeRequests { it.anyRequest().authenticated() }
                .oauth2Login()
        }
    }
}