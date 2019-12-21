package com.ryulth.offthework.api.config

import com.ryulth.offthework.api.auth.TokenInterceptor
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
        val includePatterns = listOf("/api/**")
        val excludePatterns = listOf("/api/auth/**")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(tokenInterceptor)
            .addPathPatterns(includePatterns) // auth
            .excludePathPatterns(excludePatterns)
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
