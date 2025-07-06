package com.lumen.employeeRelations.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
}




//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.media.StringSchema;
//import io.swagger.v3.oas.models.parameters.Parameter;
//import org.springdoc.core.customizers.OpenApiCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Employee Relations API")
//                        .version("1.0")
//                        .description("API documentation for managing employees and projects."));
//    }
//
//
//    @Bean
//    public OpenApiCustomizer globalHeaderCustomizer() {
//        return openApi -> openApi.getPaths().values().forEach(pathItem ->
//                pathItem.readOperations().forEach(operation -> {
//                    operation.addParametersItem(new Parameter()
//                            .in("header")
//                            .name("x-api-key")
//                            .description("API key for authentication.")
//                            .required(true)
//                            .schema(new StringSchema()._default("my-secure-key")));
//                }));
//    }
//}