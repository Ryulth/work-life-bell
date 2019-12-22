package com.ryulth.worklifebell.api.controller

import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthController {

    @ApiOperation(value = "서버 헬스 체크 API", notes = "Authorization Header 필요 없습니다. Swagger 전역 설정 원인")
    @GetMapping
    fun healthCheck(): String {
        return "Health Check"
    }
}
