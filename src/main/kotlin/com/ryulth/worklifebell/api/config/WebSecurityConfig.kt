package com.ryulth.worklifebell.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {

    @Value("\${swagger.clientId}")
    val swaggerId: String = ""
    @Value("\${swagger.clientPassword}")
    val swaggerPassword: String = ""

    companion object {
        val AUTH_LIST = arrayOf(
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
        )
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser(swaggerId).password(passwordEncoder().encode(swaggerPassword)).roles("USER", "ADMIN")
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests().antMatchers(*AUTH_LIST).authenticated().and().csrf().disable()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

}