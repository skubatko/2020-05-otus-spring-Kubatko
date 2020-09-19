package ru.skubatko.dev.otus.spring.hw28.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw28.App;
import ru.skubatko.dev.otus.spring.hw28.domain.BarberItem;
import ru.skubatko.dev.otus.spring.hw28.domain.Beauty;
import ru.skubatko.dev.otus.spring.hw28.enums.BarberItemType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Collections;

@DisplayName("Парикмахерская")
@ContextConfiguration(classes = App.class)
@ExtendWith(SpringExtension.class)
class BarberShopTest {

    @Autowired
    private BarberShop barberShop;

    @DisplayName("должна получать запрос услуг и выдавать ожидаемую красоту")
    @Test
    void shouldGetServiceRequestAndReturnExpectedBeauty() {
        BarberItemType content = BarberItemType.HAIRCUT;

        Collection<Beauty> actual = barberShop.process(Collections.singleton(new BarberItem(content)));

        assertThat(actual).isNotEmpty().hasSize(1).containsOnly(new Beauty(content.name()));
    }
}
