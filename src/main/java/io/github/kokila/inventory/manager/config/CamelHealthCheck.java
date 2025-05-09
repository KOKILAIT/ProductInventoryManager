package io.github.kokila.inventory.manager.config;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.spi.annotations.Component;

@Component("")
public class CamelHealthCheck {
    public CamelHealthCheck(ProducerTemplate producerTemplate) {
        System.out.println("✅ ProducerTemplate is injected: " + producerTemplate);
    }
}