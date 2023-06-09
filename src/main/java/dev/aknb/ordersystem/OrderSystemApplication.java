package dev.aknb.ordersystem;

import dev.aknb.ordersystem.config.ProjectConfig;
import dev.aknb.ordersystem.config.RsaKeyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableAsync(proxyTargetClass = true)
@EnableJpaAuditing
@EnableTransactionManagement
@EnableConfigurationProperties(RsaKeyConfig.class)
@SpringBootApplication(scanBasePackages = {ProjectConfig.BASE_PACKAGE})
public class OrderSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderSystemApplication.class, args);
    }

}
