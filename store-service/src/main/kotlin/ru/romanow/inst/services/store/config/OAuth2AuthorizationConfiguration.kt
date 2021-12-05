package ru.romanow.inst.services.store.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import ru.romanow.inst.services.common.config.FIRST

@Order(FIRST)
@Configuration
@EnableWebSecurity
class OAuth2AuthorizationConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .authorizeHttpRequests {
                it.antMatchers("/api/v1/store/**").authenticated()
                    .and()
                    .oauth2ResourceServer { r ->
                        r.jwt()
                    }
            }
            .authorizeHttpRequests {
                it.antMatchers("/api/v1/authorization", "/oauth2/**").authenticated()
                    .and()
                    .oauth2Login()
            }
    }
}