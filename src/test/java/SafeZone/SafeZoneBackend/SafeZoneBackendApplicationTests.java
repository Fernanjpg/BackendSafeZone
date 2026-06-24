package SafeZone.SafeZoneBackend;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.cloud.azure.cosmos.endpoint=https://localhost:8081",
		"spring.cloud.azure.cosmos.key=C2y6yDjf5/R+ob0N8A7Cgv30VRDJIWEHLM+4QDU5DE2nQ9nDuVTqobD4b8mGGyPMbIZnqyMsE4yGQ96EJGgdQ=="
})
@Disabled("Skipped because Azure Cosmos DB configuration is not available in local workspace")
class SafeZoneBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
