package com.ryulth.worklifebell.api.service

import com.ryulth.worklifebell.api.model.Location
import org.springframework.stereotype.Service

/**
 * 위치 기반으로 출근 체크 유효성 검사
 */
@Service
class LocationService {
    fun isGoToWork(location: Location): Boolean {
        return true
    }
}
