package com.msp.userservice.service;

import java.util.Map;

public interface DashboardService {
	Map<String, Object> getDashboard(Long authUserId);

}
