package com.frota_manager.inteligent_manager;

import com.frota_manager.inteligent_manager.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class InteligentManagerApplicationTests {

	@Test
	void contextLoads() {
	}

}
