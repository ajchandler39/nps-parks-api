package com.example.demo;

import com.example.demo.runner.NpsDataLoader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Smoke test: verifies the full application context wires up (controllers, services,
 * repositories, JPA, security) against in-memory H2. The startup NPS data loader is
 * replaced with a no-op mock so the test never calls the live National Park Service API.
 */
@SpringBootTest
class DemoApplicationTests {

	@MockitoBean
	NpsDataLoader npsDataLoader;

	@Test
	void contextLoads() {
	}
}
