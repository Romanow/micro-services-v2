package ru.romanow.inst.services.store.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import ru.romanow.inst.services.common.config.FIRST

@Order(FIRST)
@Configuration
class OAuth2LoginConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.requestMatchers { it.antMatchers("/api/authorization", "/oauth2/**", "/login/**") }
            .authorizeRequests { it.anyRequest().authenticated() }
            .oauth2Login()
    }
}