package ru.skubatko.dev.otus.spring.hw28.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

@Configuration
public class IntegrationConfig {

    @Bean
    public QueueChannel barberItemsChannel() {
        return MessageChannels.queue(4).get();
    }

    @Bean
    public PublishSubscribeChannel beautyChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).get();
    }

    @Bean
    public IntegrationFlow barberShopFlow() {
        return IntegrationFlows.from("barberItemsChannel")
                       .split()
                       .handle("barberService", "beautify")
                       .aggregate()
                       .channel("beautyChannel")
                       .get();
    }
}
