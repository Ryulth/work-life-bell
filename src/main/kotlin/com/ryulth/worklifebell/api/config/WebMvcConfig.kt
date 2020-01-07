package com.ryulth.worklifebell.api.config

import com.ryulth.worklifebell.api.auth.TokenInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    val tokenInterceptor: TokenInterceptor
) : WebMvcConfigurer {

    @Value("\${allowed.origin:*}")
    val origin: String = "*"

    companion object {
        val INCLUDE_PATTERNS = listOf("/api/**")
        val EXCLUDE_PATTERNS = listOf("/auth/**")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(tokenInterceptor)
            .addPathPatterns(INCLUDE_PATTERNS) // auth
            .excludePathPatterns(EXCLUDE_PATTERNS)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedHeaders("*")
            .allowedMethods("*")
            .allowedOrigins(origin)
            .allowCredentials(false)
            .maxAge(3600)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/resources/**")
            .addResourceLocations("/WEB-INF/resources/")
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }
}
