package ru.skubatko.dev.otus.spring.hw28.gateway;

import ru.skubatko.dev.otus.spring.hw28.domain.BarberItem;
import ru.skubatko.dev.otus.spring.hw28.domain.Beauty;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.Collection;

@MessagingGateway
public interface BarberShop {

    @Gateway(requestChannel = "barberItemsChannel", replyChannel = "beautyChannel")
    Collection<Beauty> process(Collection<BarberItem> orderItem);
}
