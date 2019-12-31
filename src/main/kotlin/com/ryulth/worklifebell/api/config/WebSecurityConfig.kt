package com.ryulth.worklifebell.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
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
        val SWAGGER_LIST = arrayOf(
            "/v2/api-docs" // for swagger
        )
        const val SWAGGER = "SWAGGER"
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser(swaggerId).password(passwordEncoder().encode(swaggerPassword)).roles(SWAGGER)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**")
    }
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(*SWAGGER_LIST).hasRole(SWAGGER)
            .and()
            .csrf().disable()
            .httpBasic()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

}