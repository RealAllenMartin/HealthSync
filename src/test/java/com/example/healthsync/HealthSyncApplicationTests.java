package com.example.healthsync;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HealthSyncApplicationTests {

	@Test
	void applicationClassLoads() {
		assertThat(HealthSyncApplication.class).isNotNull();
	}

}
