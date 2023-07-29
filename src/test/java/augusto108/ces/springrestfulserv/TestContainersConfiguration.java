package augusto108.ces.springrestfulserv;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = {TestContainersConfiguration.Initializer.class})
public abstract class TestContainersConfiguration {
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();

            final ConfigurableEnvironment environment = applicationContext.getEnvironment();
            final MapPropertySource testContainers = new MapPropertySource("testContainers", createConnectionConfiguration());

            environment.getPropertySources().addFirst(testContainers);
        }

        private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33");

        private static void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();
        }

        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword()
            );
        }
    }
}
