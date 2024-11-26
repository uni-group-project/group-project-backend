//package org.example.librarybackend.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.Paths;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class SwaggerConfig {
//    @Bean
//    public OpenAPI customOpenAPI() {
//        SecurityScheme jwtScheme = new SecurityScheme()
//                .name("Authorization")
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT");
//
//        var openAPI = new OpenAPI()
//                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
//                .components(new io.swagger.v3.oas.models.Components()
//                        .addSecuritySchemes("Authorization", jwtScheme));
//        Paths paths = openAPI.getPaths();
//        // TODO: List all these public paths so swagger doesn't show that funny
//        //  padlock near them
////        if (openAPI.getPaths() != null) {
////            Paths paths = openAPI.getPaths();
////            paths.get("a").getDescription();
////            paths.forEach((path, pathItem) -> {
////                if (path.startsWith("/api/public")) {
////                    // Remove security for public paths
////                    pathItem.readOperations().forEach(operation -> operation.setSecurity(null));
////                }
////            });
////        }
//        return openAPI;
//    }
//}
