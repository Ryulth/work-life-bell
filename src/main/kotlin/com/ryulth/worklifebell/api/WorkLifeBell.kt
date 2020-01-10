package com.ryulth.worklifebell.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableSwagger2
class WorkLifeBellApplication {
    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }
}

fun main(args: Array<String>) {
    runApplication<WorkLifeBellApplication>(*args)
}
