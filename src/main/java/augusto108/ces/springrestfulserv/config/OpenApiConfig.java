package augusto108.ces.springrestfulserv.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPIconfig() {
        final String title = "Guest tracker";
        final String description = "Demo application with Spring Boot HATEOAS";
        final String version = "v1";
        return new OpenAPI().info(new Info().title(title).description(description).version(version));
    }
}
