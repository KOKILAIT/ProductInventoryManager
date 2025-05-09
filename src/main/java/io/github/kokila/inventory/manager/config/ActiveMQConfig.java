package io.github.kokila.inventory.manager.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ActiveMQConfig {

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        factory.setTrustedPackages(Arrays.asList(
                "java.util",
                "java.lang",
                "io.github.kokila.inventory.manager.entity"  // <-- Trust your own package
        ));
        return factory;
    }
}
