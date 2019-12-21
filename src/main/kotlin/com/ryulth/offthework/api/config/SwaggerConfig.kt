package com.ryulth.offthework.api.config

import io.swagger.annotations.ApiOperation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.Parameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api(): Docket =
        Docket(DocumentationType.SWAGGER_2)
            .globalOperationParameters(this.globalParameters())
            .select()
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation::class.java))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(this.apiInfo())

    private fun apiInfo() =
        ApiInfoBuilder()
            .title("Off The Work API")
            .description("Off The Work API 입니다")
            .build()

    private fun globalParameters() =
        mutableListOf<Parameter>(ParameterBuilder().name("Authorization")
            .description("Access Token")
            .parameterType("header")
            .required(false)
            .defaultValue("Bearer ")
            .modelRef(ModelRef("string"))
            .build())
}
