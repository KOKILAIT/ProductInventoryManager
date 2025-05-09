package io.github.kokila.inventory.manager.config;
import io.github.kokila.inventory.manager.entity.Product;
import org.apache.camel.model.dataformat.JsonLibrary;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProductRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Route to consume messages from ActiveMQ queue
//        from("quartz://productScheduler?cron=0+0/5+*+*+*+?")
                from("activemq:queue:productQueue")
//                        .unmarshal().json(JsonLibrary.Jackson, Product.class)
                .log("Received message: ${body}")
                .to("bean:productService?method=createProduct");
    }
}
