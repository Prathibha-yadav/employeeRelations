//package com.lumen.employeeRelations.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.Paths;
//import io.swagger.v3.oas.models.PathItem;
//import io.swagger.v3.oas.models.Operation;
//import org.junit.jupiter.api.Test;
//import org.springdoc.core.customizers.OpenApiCustomizer;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.Collections;
//
//public class SwaggerConfigTest {
//
//    private final SwaggerConfig swaggerConfig = new SwaggerConfig();
//
//    @Test
//    public void testCustomOpenAPI_notNull() {
//        OpenAPI openAPI = swaggerConfig.customOpenAPI();
//        assertNotNull(openAPI);
//        assertEquals("Employee Relations API", openAPI.getInfo().getTitle());
//    }
//
//    @Test
//    public void testGlobalHeaderCustomizer_doesNotThrow() {
//        OpenApiCustomizer customizer = swaggerConfig.globalHeaderCustomizer();
//
//        // Create dummy OpenAPI object with non-null paths
//        OpenAPI openAPI = new OpenAPI();
//        Operation operation = new Operation();
//        PathItem pathItem = new PathItem().get(operation);
//        Paths paths = new Paths();
//        paths.addPathItem("/dummy", pathItem);
//        openAPI.setPaths(paths);
//
//        assertDoesNotThrow(() -> customizer.customise(openAPI));
//    }
//}