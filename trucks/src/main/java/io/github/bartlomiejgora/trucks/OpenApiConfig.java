package io.github.bartlomiejgora.trucks;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dodaje do Swagger UI przycisk "Authorize", w ktory wkleja sie token dostepu z Keycloaka.
 */
@Configuration
class OpenApiConfig {

    private static final String BEARER_SCHEME = "bearer-jwt";

    @Bean
    OpenAPI trucksOpenApi() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Token dostepu z realmu truckmanager w Keycloaku")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }
}
