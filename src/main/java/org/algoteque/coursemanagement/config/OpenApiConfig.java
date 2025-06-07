package org.algoteque.coursemanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Course Management API")
                        .version("1.0")
                        .description("API for managing educational courses")
                        .contact(new Contact()
                                .name("Algoteque")
                                .email("support@algoteque.org")
                                .url("https://algoteque.org")
                        )
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                )
                .components(new Components()
                        .addSchemas("ErrorResponse", new ObjectSchema()
                                .addProperty("message", new StringSchema().example("Course not found"))
                                .addProperty("timestamp", new StringSchema().example("2025-06-06T12:34:56"))
                        )
                        .addResponses("BadRequest", new ApiResponse()
                                .description("Validation error")
                                .content(new io.swagger.v3.oas.models.media.Content()
                                        .addMediaType("application/json", new io.swagger.v3.oas.models.media.MediaType()
                                                .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                                        )
                                )
                        )
                        .addResponses("NotFound", new ApiResponse()
                                .description("Resource not found")
                                .content(new io.swagger.v3.oas.models.media.Content()
                                        .addMediaType("application/json", new io.swagger.v3.oas.models.media.MediaType()
                                                .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                                        )
                                )
                        )
                        .addResponses("InternalError", new ApiResponse()
                                .description("Unexpected error")
                                .content(new io.swagger.v3.oas.models.media.Content()
                                        .addMediaType("application/json", new io.swagger.v3.oas.models.media.MediaType()
                                                .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                                        )
                                )
                        )
                );
    }
}
