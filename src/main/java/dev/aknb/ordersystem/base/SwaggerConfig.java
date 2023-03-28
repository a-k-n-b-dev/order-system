package dev.aknb.ordersystem.base;

import dev.aknb.ordersystem.project.ProjectConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private final ProjectConfig projectConfig;

    public SwaggerConfig(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
    }

    @Bean
    public OpenAPI swaggerOpenAPI(Info info) {

        return new OpenAPI()
                .info(info)
                .servers(List.of(
                        new Server().url(projectConfig.getServerUrl())))
                .schemaRequirement(ProjectConfig.NAME, securityScheme());
    }

    @Bean
    public Info systemInfo() {

        return new Info()
                .title("Order System APIs")
                .version("0.0.1")
                .contact(
                        new Contact()
                                .name("aknb")
                                .email("abdulazizkhan.k.n.b@gmail.com")
                                .url("https://aknb.is-a.dev")
                ).license(
                        new License()
                                .name("Apache License Version 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(ProjectConfig.NAME)
                .scheme("bearer")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER);
    }
}
