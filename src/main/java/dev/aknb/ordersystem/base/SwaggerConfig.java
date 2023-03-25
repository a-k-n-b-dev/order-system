package dev.aknb.ordersystem.base;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI swaggerOpenAPI(Info info) {

        return new OpenAPI().info(info);
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
}
